package com.example.android.photobyintent;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

public class SharePhotoActivity extends Activity {
    
    private Bitmap projectImageBitmap;
    private ImageView projectImageView;
    
    private Spinner projectNameSpinner, projectStepSpinner;
    
    private PopupWindow pw;
    private EditText name;
    private ArrayAdapter<String> projectAdapter;
    private ArrayAdapter<String> stepAdapter;
    private String[] projectNameArray;
    private String[] projectStepArray;
    
    private String editType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_share_photo);
        projectImageView = (ImageView) findViewById(R.id.projectImage);
        
        //get projectimage from photointentactivity
        Bitmap projectImage = this.getIntent().getParcelableExtra("bmp");
        projectImageView.setImageBitmap(projectImage);
       
     
        //Create project name spinner
        projectNameSpinner = (Spinner) findViewById(R.id.projectNameSpinner);
        ArrayList<String> nameArrayList = new ArrayList<String>();
        projectNameArray = getResources().getStringArray(R.array.projectNameArray);
        nameArrayList.addAll(Arrays.asList(projectNameArray));
        projectAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, nameArrayList);
        // Specify the layout to use when the list of choices appears
        projectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        projectNameSpinner.setAdapter(projectAdapter);
        
//        projectNameSpinner.setOnItemLongClickListener(new OnItemLongClickListener(){
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int arg2, long arg3){
//                return true;
//            }
//        });
        
        projectNameSpinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                            String item = parent.getItemAtPosition(position).toString(); 
                            if(item.equals("+New")){
                                editType = "project";
                                initiateNamePopupWindow();
                            }
                            //Toast.makeText(parent.getContext(), item, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
           

        //Create project step spinner
        projectStepSpinner = (Spinner) findViewById(R.id.projectStepSpinner);
        ArrayList<String> stepArrayList = new ArrayList<String>();
        projectStepArray = getResources().getStringArray(R.array.projectStepArray);
        stepArrayList.addAll(Arrays.asList(projectStepArray));
        // Create an ArrayAdapter using the string array and a default spinner layout
        stepAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, stepArrayList);
        // Specify the layout to use when the list of choices appears
        stepAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        projectStepSpinner.setAdapter(stepAdapter);
       

        projectStepSpinner.setOnItemSelectedListener(
                new OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                            String item = parent.getItemAtPosition(position).toString(); 
                            if(item.equals("+New")){
                                editType = "step";
                                initiateNamePopupWindow();
                            }
                            //Toast.makeText(parent.getContext(), item, Toast.LENGTH_LONG).show();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

               
    }
  
    private void initiateNamePopupWindow() {
            //We need to get the instance of the LayoutInflater, use the context of this activities
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //Inflate the view from a predefined XML layout
                View layout = inflater.inflate(R.layout.newproject_popup,(ViewGroup) findViewById(R.id.newProjectPopUp));
                final EditText editTextToChange = (EditText) layout.findViewById(R.id.newName);
                CharSequence hint = editTextToChange.getHint();
                Log.d("editTextHint", hint.toString());
                if(editType.equals("project")){
                    Log.d("editType:", "attempting to change edit type");
                    editTextToChange.setHint("type new project name");
                }
                else if(editType.equals("step")){
                    editTextToChange.setHint("type new step name");
                }
                // create a 500px width and 150px height PopupWindow
                pw = new PopupWindow(layout, 400, 150, true);
                // display the popup in the center
                pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
                name = (EditText)layout.findViewById(R.id.newName);
                Button cancelButton = (Button) layout.findViewById(R.id.cancel);
                Button okButton = (Button) layout.findViewById(R.id.ok);
                cancelButton.setOnClickListener(cancelButtonListener);
                okButton.setOnClickListener(okButtonListener);
        }
    
    private OnClickListener cancelButtonListener = new OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };
    
    private OnClickListener okButtonListener = new OnClickListener() {
        public void onClick(View v) {
            String nameString = name.getText().toString(); 
            Log.d("name:", nameString);
            if(editType.equals("project")){
                projectAdapter.remove("+New");
                projectAdapter.add(nameString);
                projectAdapter.add("+New");
                ArrayAdapter findNameString = (ArrayAdapter) projectNameSpinner.getAdapter();
                int spinnerPosition = findNameString.getPosition(nameString);
                projectNameSpinner.setSelection(spinnerPosition);
            }
            else if(editType.equals("step")){
                stepAdapter.remove("+New");
                stepAdapter.add(nameString);
                stepAdapter.add("+New");
                ArrayAdapter findNameString = (ArrayAdapter) projectStepSpinner.getAdapter();
                int spinnerPosition = findNameString.getPosition(nameString);
                projectStepSpinner.setSelection(spinnerPosition);
                
            }
            pw.dismiss();
        }
    };
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_share_photo, menu);
        return true;
    }
    
    

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
 
}
    
