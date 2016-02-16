package id.tech.hsmsjackettoko;

/**
 * Created by RebelCreative-A1 on 07/01/2016.
 */
public class RowData_Sms {
    String sender, message, dateArrived, viewed;
    String tv_transaction_id, tv_mason_id, tv_qty;
    String _id;

    public RowData_Sms(String sender, String message, String dateArrived, String viewed) {
        this.sender = sender;
        this.message = message;
        this.dateArrived = dateArrived;
        this.viewed = viewed;
    }

    public RowData_Sms(String sender, String message, String dateArrived, String viewed,
                       String tv_transaction_id, String tv_mason_id, String tv_qty) {
        this.sender = sender;
        this.message = message;
        this.dateArrived = dateArrived;
        this.viewed = viewed;
        this.tv_transaction_id = tv_transaction_id;
        this.tv_mason_id = tv_mason_id;
        this.tv_qty = tv_qty;
    }

    public RowData_Sms(String _id,String sender, String message, String dateArrived, String viewed,
                       String tv_transaction_id, String tv_mason_id, String tv_qty) {
        this._id = _id;
        this.sender = sender;
        this.message = message;
        this.dateArrived = dateArrived;
        this.viewed = viewed;
        this.tv_transaction_id = tv_transaction_id;
        this.tv_mason_id = tv_mason_id;
        this.tv_qty = tv_qty;
    }

    public RowData_Sms(String _id,String sender, String message, String dateArrived, String viewed) {
        this._id = _id;
        this.sender = sender;
        this.message = message;
        this.dateArrived = dateArrived;
        this.viewed = viewed;
    }
}
