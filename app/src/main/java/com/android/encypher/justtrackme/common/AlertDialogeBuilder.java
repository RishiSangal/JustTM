package com.android.encypher.justtrackme.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AlertDialogeBuilder {

	AlertDialog.Builder dialog;
	
	public interface AlertListeners{
		public abstract void Negative();
		public abstract void Positive();
	}
	
	private AlertListeners alertListner;
	Activity activity;
	public AlertDialogeBuilder(Activity activity, AlertListeners alertListner) {
		 dialog=new AlertDialog.Builder(activity);
		 this.alertListner = alertListner;
		 this.activity = activity;
		 edtText = new ArrayList<EditText>();
		 butText = new ArrayList<Button>();
		 texText = new ArrayList<TextView>();
		 chkText = new ArrayList<CheckBox>();
		 radText = new ArrayList<RadioGroup>();
	}	
	
	public AlertDialogeBuilder() {
	}

//	private static AlertDialogeBuilder instance;
//	public static AlertDialogeBuilder getInstance(){
//		if (instance == null) {
//			synchronized (AlertDialogeBuilder.class) {
//				if (instance == null) {
//					instance = new AlertDialogeBuilder();
//				}
//			}
//		}
//		return instance;
//	}
	
	public void setMultiChoiceItem(String[] list, boolean[] checkedItem, DialogInterface.OnMultiChoiceClickListener listener) {
		dialog.setMultiChoiceItems(list, checkedItem, listener);
	}
	public AlertDialogeBuilder setMessage(String message) {
		dialog.setMessage(message);
		return this;
	}
	
	public AlertDialogeBuilder setTitle(String title) {
		dialog.setTitle(title);
		return this;
	}
	public void setCancelAble(boolean cancel) {
		dialog.setCancelable(cancel);
	}
	
	View view;
	public void setView(int Resource) {
		view=activity.getLayoutInflater().inflate(Resource, null);
		dialog.setView(view);
	}
	
	ArrayList<TextView> texText;
	public TextView addTextView(TextView textview, int id) {
		textview=(TextView) view.findViewById(id);
		texText.add(textview);
		return textview;
	}

	ArrayList<EditText> edtText;
	public EditText addEditText(EditText EdtTextName, int id) {
		EdtTextName=(EditText) view.findViewById(id);
		edtText.add(EdtTextName);
		return EdtTextName;
	}

	ArrayList<Button> butText;
	public Button addButton(Button buttextName, int id) {
		buttextName=(Button) view.findViewById(id);
		butText.add(buttextName);
		return buttextName;
	}
	
	ArrayList<CheckBox> chkText;
	public CheckBox addCheckBox(CheckBox chkTextName, int id) {
		chkTextName = (CheckBox) view.findViewById(id);
		chkText.add(chkTextName);
		return chkTextName;
	}
	
	ArrayList<RadioGroup> radText;
	public RadioGroup addRadioGroup(RadioGroup radTextName, int id) {
		radTextName = (RadioGroup) view.findViewById(id);
		radText.add(radTextName);
		return radTextName;
	}
	
	public RadioButton addRadioButton(RadioButton radioButton, int id) {
		radioButton = (RadioButton) view.findViewById(id);
		return radioButton;
	}
	public ImageView addImageView (ImageView imageButton, int id) {
		imageButton = (ImageView) view.findViewById(id);
		return imageButton;
	}
	public DatePicker addDatePicker(DatePicker dateTextPicker, int id) {
		dateTextPicker = (DatePicker) view.findViewById(id);
		return dateTextPicker;
	}
	
	public RelativeLayout addRelativeLayout(RelativeLayout relativeLay, int id) {
		relativeLay = (RelativeLayout) view.findViewById(id);
		return relativeLay;
	}
	
	public AlertDialogeBuilder setNegativeButton(String canelText) {
		dialog.setNegativeButton(canelText, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				alertListner.Negative();
			}
		});
		return this;
	}
	public AlertDialogeBuilder setPositiveButton(String goText) {
		dialog.setPositiveButton(goText, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int position) {
				alertListner.Positive();
			}
		});
		return this;
	}

	public void createAlert() {
		dialog.create().show();
	}
	
	AlertDialog d1= null;
	public void createAlertChangeProperty() {
		d1 = dialog.create();
		d1.show();
	}
	
	public void dismissAlert() {
		d1.dismiss();
	}
	public AlertDialog getAlertDialog() {
		return d1;
	}
	public AlertDialog.Builder getAlertDialogBuilder() {
		return dialog;
	}
	
}
