package com.flight.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import f21.wechat.flight.R;

public class MainDialogFragment extends DialogFragment{
	
	private Button mBtnRestart, mBtnQuit;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_main, null);
		
		mBtnRestart = (Button) view.findViewById(R.id.btn_restart);
		mBtnQuit = (Button) view.findViewById(R.id.btn_quit);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setView(view);
		
		mBtnRestart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getActivity() instanceof AlterInputListener) {
					((AlterInputListener)getActivity()).onAlertInputComplete(true);
					dismiss();
				}
			}
		});
		
		mBtnQuit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (getActivity() instanceof AlterInputListener) {
					((AlterInputListener)getActivity()).onAlertInputComplete(false);
					dismiss();
				}
			}
		});
		
		return dialog.create();
	}
	
	public interface AlterInputListener{
		void onAlertInputComplete(boolean isRestart);
	}
}
