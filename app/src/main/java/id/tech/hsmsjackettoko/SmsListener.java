package id.tech.hsmsjackettoko;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.transition.Slide;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import id.tech.orm_sugar.SLite;

/**
 * Created by RebelCreative-A1 on 06/01/2016.
 */
public class SmsListener extends BroadcastReceiver {
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())){
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if(bundle != null){
                try{
                    Object[] pdus = (Object[])bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Log.e("toko SMS from = ", msg_from);
                        Log.e("toko SMS message = ", msgBody);

                        if(msg_from.equals(Parameter_Collections.nomer_holcim) ||
                                msg_from.equals(Parameter_Collections.nomer_holcim_2)) {

                            // if "Apakah" maka input baru, jika "Konfirmasi" maka update
//                            String patokan = msgBody.substring(0, 3);
//                            Log.e("patokan", patokan);
                            String patokanNews = msgBody.substring(msgBody.length()-3, msgBody.length());
                            Log.e("patokan News = ", patokanNews);

                            if (msgBody.contains("Apakah")) {
                                //get string MasonIdnya
                                String sms_apakah = msgBody;
                                Log.e("TOKO SMS APAKAH ", sms_apakah);
                                int loc_mandorid_apakah = sms_apakah.indexOf("Mandor ");
                                String mandor_berhasil = sms_apakah.substring(loc_mandorid_apakah + 7, loc_mandorid_apakah + 16);
                                Log.e("TOKO SMS APAKAH TrxId", mandor_berhasil);

                                int loc_trxid_apakah = sms_apakah.indexOf("kode transaksi : ");
                                String trxId_berhasil = sms_apakah.substring(loc_trxid_apakah + 17, loc_trxid_apakah + 22);
                                Log.e("TOKO SMS APAKAH TrxId", trxId_berhasil);
                                Log.e("TOKO SMS APAKAH ALL", "#"+ mandor_berhasil+ "#" + trxId_berhasil + "#");

                                //Editan to Uppercast
                                trxId_berhasil = trxId_berhasil.toUpperCase();

                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);

                                //datanya
                                cv.put("confirmCode", "1");
                                cv.put("masonId", mandor_berhasil);
                                cv.put("trxId", trxId_berhasil);

                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());

                                cv.put("dateReceived", date_now);
                                cv.put("qty", "0");
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_mandor", null, cv);
                                db.close();

                                Log.e("TOKO SMS masonId = ", mandor_berhasil);
                                Log.e("TOKO SMS trxId", trxId_berhasil);
                                Log.e("TOKO SMS Holcim Claim", "Inputed to DB");

                            } else if (msgBody.contains("Konfirmasi")) {
                                String sms_konfirmasi = msgBody;
                                Log.e("TOKO SMS KONFIRMASI ", sms_konfirmasi);
                                int loc_trxid_berhasil = sms_konfirmasi.indexOf("TrxId: ");
                                String trxId_berhasil = sms_konfirmasi.substring(loc_trxid_berhasil + 7, loc_trxid_berhasil + 12);

                                //Editan to Uppercast
                                trxId_berhasil = trxId_berhasil.toUpperCase();

                                Log.e("TOKO SMS KONFIR TrxId", trxId_berhasil);
                                Log.e("TOKO SMS KONFIR ALL", "#" + trxId_berhasil + "#");

                                //jika berhasil maka update
                                if (msgBody.contains("Berhasil")) {
                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("confirmCode", "2");

                                    final Cursor c = db.query("tbl_sms_mandor", new String[] {"trxId", "id"}
                                            ,null, null, null, null,"dateReceived DESC", null);

                                    while(c.moveToNext()){
                                        if(c.getString(0).equals(trxId_berhasil)){
                                            db.update("tbl_sms_mandor", cv, "id = ? ", new String[]{c.getString(1)});
                                            Log.e("TOKO SMS KONFIR", "BERHASIL 1 Updated trxId = " + trxId_berhasil + " id = " + c.getString(1));
                                        }
                                    }

                                    db.close();
                                } else if(msgBody.contains("berhasil di Konfirmasi")){
                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("confirmCode", "2");
                                    final Cursor c = db.query("tbl_sms_mandor", new String[] {"trxId", "id"}
                                            ,null, null, null, null,"dateReceived DESC", null);

                                    while(c.moveToNext()){
                                        if(c.getString(0).equals(trxId_berhasil)){
                                            db.update("tbl_sms_mandor", cv, "id = ? ", new String[]{c.getString(1)});
                                            Log.e("TOKO SMS KONFIR", "BERHASIL 1 Updated trxId = " + trxId_berhasil + " id = " + c.getString(1));
                                        }
                                    }
                                    db.close();
                                }else if(msgBody.contains("mengkonfirmasi ulang")){
                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("confirmCode", "2");
                                    final Cursor c = db.query("tbl_sms_mandor", new String[] {"trxId", "id"}
                                            ,null, null, null, null,"dateReceived DESC", null);

                                    while(c.moveToNext()){
                                        if(c.getString(0).equals(trxId_berhasil)){
                                            db.update("tbl_sms_mandor", cv, "id = ? ", new String[]{c.getString(1)});
                                            Log.e("TOKO SMS KONFIR", "BERHASIL 1 Updated trxId = " + trxId_berhasil + " id = " + c.getString(1));
                                        }
                                    }
                                    db.close();
                                }else{
                                    Log.e("TOKO SMS KONFIR", "GAGAL Updated trxId = " + trxId_berhasil);
                                }

                            }else if (msgBody.contains("Terima kasih")) {
                                String sms_konfirmasi = msgBody;
                                Log.e("TOKO SMS KONFIRMASI ", sms_konfirmasi);
                                int loc_trxid_berhasil = sms_konfirmasi.indexOf("TrxId: ");
                                String trxId_berhasil = sms_konfirmasi.substring(loc_trxid_berhasil + 7, loc_trxid_berhasil + 12);

                                //Editan to Uppercast
                                trxId_berhasil = trxId_berhasil.toUpperCase();

                                Log.e("TOKO SMS KONFIR TrxId", trxId_berhasil);
                                Log.e("TOKO SMS KONFIR ALL", "#" + trxId_berhasil + "#");

                                //jika berhasil maka update
                                if(msgBody.contains("mengkonfirmasi ulang")){
                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("confirmCode", "2");
                                    final Cursor c = db.query("tbl_sms_mandor", new String[] {"trxId", "id"}
                                            ,null, null, null, null,"dateReceived DESC", null);

                                    while(c.moveToNext()){
                                        if(c.getString(0).equals(trxId_berhasil)){
                                            db.update("tbl_sms_mandor", cv, "id = ? ", new String[]{c.getString(1)});
                                            Log.e("TOKO SMS KONFIR", "BERHASIL 1 Updated trxId = " + trxId_berhasil + " id = " + c.getString(1));
                                        }
                                    }
                                    db.close();
                                }else{
                                    Log.e("TOKO SMS KONFIR", "GAGAL Updated trxId = " + trxId_berhasil);
                                }

                            }else if(patokanNews.equals("ama")) {
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());
                                cv.put("dateReceived", date_now);
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_news", null, cv);
                                db.close();
                                Log.e("TOKO SMS", "SMS NEWS");
                            } else {
                                //masuk ke tabel inbox biasa
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                cv.put("confirmCode", "1");
                                cv.put("masonId", "0");
                                cv.put("trxId", "0");

                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());

                                cv.put("dateReceived", date_now);
                                cv.put("qty", "0");
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_inbox", null, cv);
                                db.close();
                                Log.e("TOKO SMS", "SMS biasa INBOX");
                            }
                        }else if(msgBody.contains("(Holcim")){
                            String patokanNews = msgBody.substring(msgBody.length() - 3, msgBody.length());
                            Log.e("patokan News = ", patokanNews);

                            if (msgBody.contains("Apakah")) {
                                //get string MasonIdnya
                                String sms_apakah = msgBody;
                                Log.e("TOKO SMS APAKAH ", sms_apakah);
                                int loc_mandorid_apakah = sms_apakah.indexOf("Mandor ");
                                String mandor_berhasil = sms_apakah.substring(loc_mandorid_apakah + 7, loc_mandorid_apakah + 16);

                                Log.e("TOKO SMS APAKAH TrxId", mandor_berhasil);

                                int loc_trxid_apakah = sms_apakah.indexOf("kode transaksi : ");
                                String trxId_berhasil = sms_apakah.substring(loc_trxid_apakah + 17, loc_trxid_apakah + 22);
                                //Editan to Uppercast
                                trxId_berhasil = trxId_berhasil.toUpperCase();

                                Log.e("TOKO SMS APAKAH TrxId", trxId_berhasil);
                                Log.e("TOKO SMS APAKAH ALL", "#"+ mandor_berhasil+ "#" + trxId_berhasil + "#");

                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);

                                //datanya
                                cv.put("confirmCode", "1");
                                cv.put("masonId", mandor_berhasil);
                                cv.put("trxId", trxId_berhasil);

                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());

                                cv.put("dateReceived", date_now);
                                cv.put("qty", "0");
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_mandor", null, cv);
                                db.close();

                                Log.e("TOKO SMS masonId = ", mandor_berhasil);
                                Log.e("TOKO SMS trxId", trxId_berhasil);
                                Log.e("TOKO SMS Holcim Claim", "Inputed to DB");

                            } else if (msgBody.contains("Konfirmasi")) {
                                String sms_konfirmasi = msgBody;
                                Log.e("TOKO SMS KONFIRMASI ", sms_konfirmasi);
                                int loc_trxid_berhasil = sms_konfirmasi.indexOf("TrxId: ");
                                String trxId_berhasil = sms_konfirmasi.substring(loc_trxid_berhasil + 7, loc_trxid_berhasil + 12);
                                //Editan to Uppercast
                                trxId_berhasil = trxId_berhasil.toUpperCase();

                                Log.e("TOKO SMS KONFIR TrxId", trxId_berhasil);
                                Log.e("TOKO SMS KONFIR ALL", "#" + trxId_berhasil + "#");

                                //jika berhasil maka update
                                if (msgBody.contains("Berhasil")) {
                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("confirmCode", "2");

                                    final Cursor c = db.query("tbl_sms_mandor", new String[] {"trxId", "id"}
                                            ,null, null, null, null,"dateReceived DESC", null);

                                    while(c.moveToNext()){
                                        if(c.getString(0).equals(trxId_berhasil)){
                                            db.update("tbl_sms_mandor", cv, "id = ? ", new String[]{c.getString(1)});
                                            Log.e("TOKO SMS KONFIR", "BERHASIL 1 Updated trxId = " + trxId_berhasil + " id = " + c.getString(1));
                                        }
                                    }

                                    db.close();
                                } else if(msgBody.contains("berhasil di Konfirmasi")){
                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("confirmCode", "2");
                                    final Cursor c = db.query("tbl_sms_mandor", new String[] {"trxId", "id"}
                                            ,null, null, null, null,"dateReceived DESC", null);

                                    while(c.moveToNext()){
                                        if(c.getString(0).equals(trxId_berhasil)){
                                            db.update("tbl_sms_mandor", cv, "id = ? ", new String[]{c.getString(1)});
                                            Log.e("TOKO SMS KONFIR", "BERHASIL 1 Updated trxId = " + trxId_berhasil + " id = " + c.getString(1));
                                        }
                                    }
                                    db.close();
                                }else{
                                    Log.e("TOKO SMS KONFIR", "GAGAL Updated trxId = " + trxId_berhasil);
                                }

                            }else if (msgBody.contains("Terima kasih")) {
                                String sms_konfirmasi = msgBody;
                                Log.e("TOKO SMS KONFIRMASI ", sms_konfirmasi);
                                int loc_trxid_berhasil = sms_konfirmasi.indexOf("TrxId: ");
                                String trxId_berhasil = sms_konfirmasi.substring(loc_trxid_berhasil + 7, loc_trxid_berhasil + 12);

                                //Editan to Uppercast
                                trxId_berhasil = trxId_berhasil.toUpperCase();

                                Log.e("TOKO SMS KONFIR TrxId", trxId_berhasil);
                                Log.e("TOKO SMS KONFIR ALL", "#" + trxId_berhasil + "#");

                                //jika berhasil maka update
                                if(msgBody.contains("mengkonfirmasi ulang")){
                                    SQLiteDatabase db = SLite.openDatabase(context);
                                    ContentValues cv = new ContentValues();
                                    cv.put("confirmCode", "2");
                                    final Cursor c = db.query("tbl_sms_mandor", new String[] {"trxId", "id"}
                                            ,null, null, null, null,"dateReceived DESC", null);

                                    while(c.moveToNext()){
                                        if(c.getString(0).equals(trxId_berhasil)){
                                            db.update("tbl_sms_mandor", cv, "id = ? ", new String[]{c.getString(1)});
                                            Log.e("TOKO SMS KONFIR", "BERHASIL 1 Updated trxId = " + trxId_berhasil + " id = " + c.getString(1));
                                        }
                                    }
                                    db.close();
                                }else{
                                    Log.e("TOKO SMS KONFIR", "GAGAL Updated trxId = " + trxId_berhasil);
                                }

                            }else if(patokanNews.equals("ama")) {
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());
                                cv.put("dateReceived", date_now);
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_news", null, cv);
                                db.close();
                                Log.e("TOKO SMS", "SMS NEWS");
                            } else {
                                //masuk ke tabel inbox biasa
                                SQLiteDatabase db = SLite.openDatabase(context);
                                ContentValues cv = new ContentValues();
                                cv.put("senderId", msg_from);
                                cv.put("bodyMessage", msgBody);
                                cv.put("confirmCode", "1");
                                cv.put("masonId", "0");
                                cv.put("trxId", "0");

                                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
                                String date_now = df.format(Calendar.getInstance().getTime());

                                cv.put("dateReceived", date_now);
                                cv.put("qty", "0");
                                cv.put("viewed", "0");
                                db.insert("tbl_sms_inbox", null, cv);
                                db.close();
                                Log.e("TOKO SMS", "SMS biasa INBOX");
                            }
                        }else{
                            Log.e("Toko SMS ", "sms biasa dari Holcim");
                        }




                    }
                }catch(Exception e){
                    Log.d("Exception caught",e.getMessage());
                }
            }
        }
    }
}
