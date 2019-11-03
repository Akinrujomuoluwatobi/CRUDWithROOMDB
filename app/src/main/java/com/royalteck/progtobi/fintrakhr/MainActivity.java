package com.royalteck.progtobi.fintrakhr;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.royalteck.progtobi.fintrakhr.Adapter.StaffsListAdapter;
import com.royalteck.progtobi.fintrakhr.LocalDB.AppDatabase;
import com.royalteck.progtobi.fintrakhr.model.UserModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    AppDatabase mAppDatabase;
    UserModel userModel;
    RecyclerView mRecyclerView;
    StaffsListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAppDatabase = AppDatabase.getAppDatabase(this);
        mRecyclerView = findViewById(R.id.usersRecycler);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDialog(true, null, 0);
            }
        });

        fetchUsers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launchDialog(final boolean newStaffAct, final UserModel obj, final Integer id) {
        final Dialog dialog = new Dialog(this);
        //.bind(this, dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.register_staff_dialog);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.findViewById(R.id.bt_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button delete = dialog.findViewById(R.id.deletestaffbtn);

        Button registerorupdate = dialog.findViewById(R.id.updatestaffBtn);
        if (newStaffAct) {
            delete.setVisibility(View.INVISIBLE);
        }
        final EditText staffName = dialog.findViewById(R.id.staff_name);
        final EditText staffPosition = dialog.findViewById(R.id.staff_position);
        final EditText staffSalary = dialog.findViewById(R.id.staff_salary);
        if (obj != null){
            staffName.setText(obj.getName());
            staffPosition.setText(obj.getPosition());
            staffSalary.setText(obj.getSalary());
        }

        registerorupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateFields(staffName, staffPosition, staffSalary))
                    return;
                userModel = new UserModel();
                userModel.setName(staffName.getText().toString());
                userModel.setPosition(staffPosition.getText().toString());
                userModel.setSalary(staffSalary.getText().toString());
                new AsyncTask<Boolean, Object, Object>() {
                    @Override
                    protected Boolean doInBackground(Boolean... objects) {
                        Boolean value = objects[0];
                        if (value) {
                            mAppDatabase.mPatrecDAO().insertToUser(userModel);
                        } else {
                            mAppDatabase.mPatrecDAO().updateUser(userModel.getName(), userModel.getPosition(), userModel.getSalary(), obj.getId());
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        dialog.dismiss();
                        fetchUsers();
                    }
                }.execute(newStaffAct);


            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AsyncTask<String, Object, Object>() {
                    @Override
                    protected Object doInBackground(String... strings) {
                        mAppDatabase.mPatrecDAO().deleteUser(id);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Object o) {
                        super.onPostExecute(o);
                        dialog.dismiss();
                        fetchUsers();

                    }
                }.execute();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void fetchUsers() {
        new AsyncTask<Object, Object, List<UserModel>>() {
            @Override
            protected List<UserModel> doInBackground(Object... objects) {
                List<UserModel> userModels = new ArrayList<>();
                userModels.clear();
                userModels = mAppDatabase.mPatrecDAO().fetchUsers();
                return userModels;
            }

            @Override
            protected void onPostExecute(List<UserModel> userModels) {
                super.onPostExecute(userModels);
                mAdapter = new StaffsListAdapter(MainActivity.this, userModels);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                mRecyclerView.setLayoutManager(layoutManager);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnClickListener(new StaffsListAdapter.OnClickListener() {
                    @Override
                    public void onItemClick(View view, UserModel obj, int pos) {
                        launchDialog(false, obj, obj.getId());

                    }

                    @Override
                    public void onItemLongClick(View view, UserModel obj, int pos) {

                    }
                });

            }
        }.execute();
    }

    private boolean validateFields(EditText staffname, EditText staffposition, EditText staffsalary) {

        boolean valid = true;

        String staffName = staffname.getText().toString();
        String staffPosition = staffposition.getText().toString();
        String staffSalary = staffsalary.getText().toString();

        if (staffName.isEmpty()/* || !android.util.Patterns.staffID_ADDRESS.matcher(staffID).matches()*/) {
            staffname.setError("* Field is Required");
            requestFocus(staffname);
            valid = false;
        } else {
            staffname.setError(null);
        }

        if (staffPosition.isEmpty()/* || !android.util.Patterns.staffID_ADDRESS.matcher(staffID).matches()*/) {
            staffposition.setError("* Field is Required");
            requestFocus(staffposition);
            valid = false;
        } else {
            staffposition.setError(null);
        }

        if (staffSalary.isEmpty()/* || !android.util.Patterns.staffID_ADDRESS.matcher(staffID).matches()*/) {
            staffsalary.setError("* Field is Required");
            requestFocus(staffsalary);
            valid = false;
        } else {
            staffsalary.setError(null);
        }

        return valid;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}