package db;

import android.content.ContentValues;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Locale;

import model.Employee;

/**
 * Created by suporte on 1/19/17.
 */

public class EmployeeDAO extends EmployeeDBDAO {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public EmployeeDAO(Context context) {
        super(context);
    }

    public long save(Employee employee){
        ContentValues values = new ContentValues();
        values.put(DataBaseHelper.NAME_COLUMN, employee.getName());
        values.put(DataBaseHelper.EMPLOYEE_DOB, formatter.format(employee.getDateOfBirth()));
        values.put(DataBaseHelper.EMPLOYEE_SALARY, employee.getSalary());

        return database.insert(DataBaseHelper.EMPLOYEE_TABLE, null, values);
    }
}
