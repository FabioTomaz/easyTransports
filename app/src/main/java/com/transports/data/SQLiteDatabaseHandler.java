package com.transports.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.transports.tickets.Ticket;
import com.transports.tickets.TicketGlobal;
import com.transports.utils.Constants;
import com.transports.utils.UtilityFunctions;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TicketsDB";
    private static final String TICKET_TABLE_NAME = "Tickets";
    private static final String GLOBAL_TICKET_TABLE_NAME = "GlobalTickets";
    private static final String[] GLOBAL_TICKET_COLUMNS = {"id", "originDestination", "schedule", "transports", "userID"};
    private static final String[] TICKET_COLUMNS = {"id", "details", "state", "global_ticket"};

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creates two tables: one for global tickets, another for individiual tickets.
        //Individual tickets have a foreigner key for global tickets
        String CREATION_TICKET_GLOBAL_TABLE = "CREATE TABLE "+GLOBAL_TICKET_TABLE_NAME+" ( "
                + GLOBAL_TICKET_COLUMNS[0]+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GLOBAL_TICKET_COLUMNS[1]+" TEXT, "
                + GLOBAL_TICKET_COLUMNS[2]+" TEXT, "
                + GLOBAL_TICKET_COLUMNS[3]+" TEXT, "
                + GLOBAL_TICKET_COLUMNS[4]+" TEXT)" ;

        db.execSQL(CREATION_TICKET_GLOBAL_TABLE);

        String CREATION_TICKET_TABLE = "CREATE TABLE "+TICKET_TABLE_NAME+" ( "
                + "id INTEGER PRIMARY KEY, "
                + "details TEXT, "
                + "state TEXT, "
                + "global_ticket INTEGER ,"
                + " FOREIGN KEY (global_ticket) REFERENCES "+GLOBAL_TICKET_TABLE_NAME+"(id));";

        db.execSQL(CREATION_TICKET_TABLE);

        //TODO: create ticket history table
    }

    @Override public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + GLOBAL_TICKET_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TICKET_TABLE_NAME);
        this.onCreate(db);
    }

    /**
     * Get all global tickets that are not yet used (that is, there is at least one ticket that is valid)
     * MIGHT NOT BE NECESSARY
     * @return
     */
    public List<Ticket> getAllUserTickets() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Ticket> tickets = new ArrayList<>();
        Cursor  cursor = db.rawQuery("select * from "+TICKET_TABLE_NAME,null);

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(TICKET_COLUMNS[0]));
                String details = cursor.getString(cursor.getColumnIndex(TICKET_COLUMNS[1]));
                String status = cursor.getString(cursor.getColumnIndex(TICKET_COLUMNS[1]));
                Ticket t = new Ticket(id, details, status);
                tickets.add(t);
                cursor.moveToNext();
            }
        }
        return tickets;
    }

    /**
     * Get all tickets that MAY not be used from a user
     * @return
     */
    public List<TicketGlobal> getAllGlobalTickets(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<TicketGlobal> ticketGlobals = new ArrayList<>();
        Cursor  cursor = db.rawQuery("select * from "+GLOBAL_TICKET_TABLE_NAME +" where "+ GLOBAL_TICKET_COLUMNS[4] +" = ?",new String[]{userID});

        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                int id = cursor.getInt(cursor.getColumnIndex(GLOBAL_TICKET_COLUMNS[0]));
                Log.d("getGlobalTicketsDB", id+"");
                String originDestination = cursor.getString(cursor.getColumnIndex(GLOBAL_TICKET_COLUMNS[1]));
                String schedule = cursor.getString(cursor.getColumnIndex(GLOBAL_TICKET_COLUMNS[2]));
                String transports = cursor.getString(cursor.getColumnIndex(GLOBAL_TICKET_COLUMNS[3]));
                //String date = cursor.getString(cursor.getColumnIndex(GLOBAL_TICKET_COLUMNS[4]));
                List<Ticket> tickets = getTicketsFromGlobalTicket(id);
                TicketGlobal tg = new TicketGlobal(originDestination, transports, schedule, tickets);
                if (allTicketsExpired(tickets)){
                    //delete tickets and global ticket from DB
                    deleteOneGlobalTicket(tg);
                }else {
                    ticketGlobals.add(tg);
                }
                cursor.moveToNext();
            }
        }
        return ticketGlobals;
    }

    private boolean allTicketsExpired(List<Ticket> tickets){
        for (Ticket t : tickets){
            if (!t.getState().equalsIgnoreCase(Constants.TICKET_EXPIRED)){
                return false;
            }
        }
        return true;
    }

    /**
     * Given a global ticket id, return from the db all the tickets that belong to that global ticket
     * @param globalTicketID
     * @return
     */
    public List<Ticket> getTicketsFromGlobalTicket(int globalTicketID){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Ticket> tickets = new ArrayList<>();
        Cursor cursor = db.query(TICKET_TABLE_NAME, // a. table
                TICKET_COLUMNS, // b. column names
                TICKET_COLUMNS[3]+" = ?", // c. selections
                new String[] { String.valueOf(globalTicketID) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor.moveToFirst()) {
            do {
                String details = cursor.getString(cursor.getColumnIndex(TICKET_COLUMNS[1]));
                Ticket ticket = UtilityFunctions.parseJsonToTicket(details);
                tickets.add(ticket);
            } while(cursor.moveToNext());
        }
        return tickets;
    }


    /**
     * Add a global ticket, which includes one or more ticket. Therefore one entry is added on the global tickets tabel
     * and at least one entry is added on the tickets table
     * @param ticketGlobal
     */
    public void addGlobalTicket(TicketGlobal ticketGlobal, String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GLOBAL_TICKET_COLUMNS[1], ticketGlobal.getOriginDestination());
        values.put(GLOBAL_TICKET_COLUMNS[2], ticketGlobal.getSchedule());
        values.put(GLOBAL_TICKET_COLUMNS[3], ticketGlobal.getTransports());
        values.put(GLOBAL_TICKET_COLUMNS[4], userID);
        // insert
        int id = (int) db.insert(GLOBAL_TICKET_TABLE_NAME,null, values); //TODO: does it actually return that id in column "id" or just and id of the row?
        //if no good, use this
        //Cursor cursor = theDatabase.query(DATABASE_TABLE, columns,null, null, null, null, null);
        //cursor.moveToLast();
        Log.d("dbID", "inserted global ticket id: "+id);
        //now insert each individual ticket
        values.clear();
        for (Ticket ticket : ticketGlobal.getTickets()){
            values.put(TICKET_COLUMNS[0], ticket.getId());
            values.put(TICKET_COLUMNS[1], ticket.getDetails());
            values.put(TICKET_COLUMNS[2], ticket.getState());
            values.put(TICKET_COLUMNS[3], id);
            db.insert(TICKET_TABLE_NAME,null, values);
            values.clear();
        }
        db.close();
    }

    public int updateTicketState(int ticketID, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        //get details and update
        Ticket t = getTicketByID(ticketID);
        String detailsUpdated = UtilityFunctions.updateStatusJSON(t, status);
        values.put(TICKET_COLUMNS[2], status);
        values.put(TICKET_COLUMNS[1], detailsUpdated);

        int i = db.update(TICKET_TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[] { String.valueOf(ticketID) });

        db.close();
        return i;
    }

    public Ticket getTicketByID(int ticketID){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TICKET_TABLE_NAME, // a. table
                TICKET_COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[] { String.valueOf(ticketID) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
        Ticket t = null;
        if (cursor != null) {
            cursor.moveToFirst();
            String details = cursor.getString(cursor.getColumnIndex(TICKET_COLUMNS[1]));
            int id = cursor.getInt(cursor.getColumnIndex(TICKET_COLUMNS[0]));
            String status = cursor.getString(cursor.getColumnIndex(TICKET_COLUMNS[2]));
            t = new Ticket(id, details, status);
        }
        return t;
    }

    public void deleteOneGlobalTicket(TicketGlobal globalTicket) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        //delete all single tickets
        db.delete(TICKET_TABLE_NAME,TICKET_COLUMNS[3]+"=?",new String[]{globalTicket.getId()+""});
        db.delete(GLOBAL_TICKET_TABLE_NAME, GLOBAL_TICKET_COLUMNS[0]+"= ?", new String[] { String.valueOf(globalTicket.getId()) });
        //delete on table tickets as well (delete cascade??)
        db.close();
    }

}