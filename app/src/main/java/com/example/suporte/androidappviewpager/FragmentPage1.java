package com.example.suporte.androidappviewpager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import db.EmployeeDAO;
import model.Employee;


/**
 * Created by suporte on 1/17/17.
 */

public class FragmentPage1 extends Fragment implements OnClickListener {
    // ui references
    private EditText empNameEtxt;
    private EditText empSalaryEtxt;
    private EditText empDobEtxt;
    private Button addButton;
    private Button resetButton;

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    DatePickerDialog datePickerDialog;
    Calendar dateCalendar;
    Employee employee = null;
    private EmployeeDAO employeeDAO;
    private AddEmpTask task;

    private static final String ARG_ITEM_ID = "emp_add_fragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        employeeDAO = new EmployeeDAO(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page1, container, false);

        findViewsById(rootView);

        setListeners();

        // for orientation change.
        if (savedInstanceState != null){
            dateCalendar = Calendar.getInstance();
            if (savedInstanceState.getLong("dateCalendar") != 0)
                dateCalendar.setTime(new Date(savedInstanceState.getLong("dateCaendar")));
        }

        return rootView;
    }

    private void setListeners() {
        empDobEtxt.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateCalendar = Calendar.getInstance();
                dateCalendar.set(year, monthOfYear, dayOfMonth);
                empDobEtxt.setText(formatter.format(dateCalendar.getTime()));
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        addButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    protected void resetAllFields(){
        empNameEtxt.setText("");
        empSalaryEtxt.setText("");
        empDobEtxt.setText("");
    }

    private void setEmployee(){
        employee = new Employee();
        employee.setName(empNameEtxt.getText().toString());
        employee.setSalary(Double.parseDouble(empSalaryEtxt.getText().toString()));
        if (dateCalendar != null)
            employee.setDateOfBirth(dateCalendar.getTime());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (dateCalendar != null)
            outState.putLong("dateCalendar", dateCalendar.getTime().getTime());
    }

    private void findViewsById(View rootView) {

        empNameEtxt = (EditText)rootView.findViewById(R.id.etxt_name);
        empSalaryEtxt = (EditText)rootView.findViewById(R.id.etxt_salary);
        empDobEtxt = (EditText)rootView.findViewById(R.id.etxt_dob);
        empDobEtxt.setInputType(InputType.TYPE_NULL);

        addButton = (Button)rootView.findViewById(R.id.button_add);
        resetButton = (Button)rootView.findViewById(R.id.button_reset);
    }

    @Override
    public void onClick(View view) {
        if (view == empDobEtxt){
            datePickerDialog.show();
        }
        else if (view == addButton){
            setEmployee();

            task = new AddEmpTask(getActivity());
            task.execute((Void) null);
        }
        else if (view == resetButton){
            resetAllFields();
        }

    }

    private class AddEmpTask extends AsyncTask<Void, Void, Long> {
        private final WeakReference<Activity> activityWeakRef;

        public AddEmpTask(Activity context) {

            activityWeakRef = new WeakReference<Activity>(context);
        }

        @Override
        protected Long doInBackground(Void... voids) {

            long result = employeeDAO.save(employee);
            return result;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);

            if (activityWeakRef.get() != null && !activityWeakRef.get().isFinishing()){
                if (result != -1)
                    Toast.makeText(activityWeakRef.get(), "Employee Saved", Toast.LENGTH_LONG).show();
            }
        }
    }
}
