package database;

import android.content.Context;
import android.database.Cursor;

public class DatabaseInteractionActivity extends Snowflake {

	public DatabaseInteractionActivity(Context ctxt) {
		super(ctxt);
	}

	public void open() {
		super.open();
	}
	
	public void close() {
		super.close();
	}
	
	public void select_columns(String table, String[] columns) {
		super.setColumns(table, columns);
	}
	
	public Cursor select_row(String selection, String[] args) {
		Cursor returnedCursor = super.select(selection, args);
		return returnedCursor;
	}
}
