/**
 * Copyright (c) 2012-2015 Magnet Systems. All rights reserved.
 */
package com.jim.apps.twitter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine;

public class ComposeDialogFragment extends DialogFragment {

  private static final String KEY_IN_REPLY_ID = "inReplyId";
  private static final String KEY_IN_REPLY_NAME = "inReplySreenName";

  private static final int MAX_LENGTH = 140;

  private BlurDialogEngine mBlurEngine;

  private OnNewTweetListener newTweetListener;

  private Long inReplyId;
  private String inReplySreenName;

  public static ComposeDialogFragment newInstance(Long inReplyId, String inReplySreenName) {
    ComposeDialogFragment fragment = new ComposeDialogFragment();

    if(null != inReplyId) {
      Bundle args = new Bundle();
      args.putLong(KEY_IN_REPLY_ID, inReplyId);
      args.putString(KEY_IN_REPLY_NAME, inReplySreenName);
      fragment.setArguments(args);
    }

    return fragment;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    this.newTweetListener = (OnNewTweetListener) activity;
  }

  @Override
  public void onDetach() {
    super.onDetach();
    this.newTweetListener = null;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //setStyle(DialogFragment.STYLE_NORMAL, R.style.FULLSCREEN_WITH_TITLE_DIALOG);
    setStyle(DialogFragment.STYLE_NO_TITLE, R.style.PopDialog);

    mBlurEngine = new BlurDialogEngine(getActivity());
    //mBlurEngine.debug(true);
    mBlurEngine.setBlurRadius(7);
    mBlurEngine.setDownScaleFactor(5f);
    mBlurEngine.setBlurActionBar(true);
    mBlurEngine.setUseRenderScript(true);
  }

  @Override
  public void onStart() {
    super.onStart();
    Dialog d = getDialog();
    if (d!=null){


      //d.getWindow().setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

      //d.getWindow().setBackgroundDrawable(null);
    }
  }

  //http://stackoverflow.com/questions/13257038/custom-layout-for-dialogfragment-oncreateview-vs-oncreatedialog
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    View view = inflater.inflate(R.layout.fragment_compose, container);
    final EditText etText = (EditText) view.findViewById(R.id.edText);

    if(null != getArguments()) {
      inReplyId = getArguments().getLong(KEY_IN_REPLY_ID);
      if (null != inReplyId) {
        etText.setText("@" + getArguments().getString(KEY_IN_REPLY_NAME));
      }
    }

    Button btnTweet = (Button) view.findViewById(R.id.btnTweet);
    btnTweet.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(null == inReplyId) {
          newTweetListener.onNewTweet(etText.getText().toString());
        } else {
          newTweetListener.onReplyTweet(inReplyId, etText.getText().toString());
        }
        dismiss();
      }
    });

    Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ComposeDialogFragment.this.dismiss();
      }
    });

    final TextView tvCharCount = (TextView) view.findViewById(R.id.tvCharCount);

    etText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Log.d("TextView", "----------beforeTextChanged : " + s.toString());
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        //Log.d("TextView", "----------onTextChanged     : " + s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {
        //Log.d("TextView", "----------afterTextChanged  : " + s.toString());
        tvCharCount.setText(String.valueOf(MAX_LENGTH - etText.getText().toString().length()));
      }

    });

    ImageView ivClose = (ImageView) view.findViewById(R.id.ivCloseDialog);
    ivClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ComposeDialogFragment.this.dismiss();
      }
    });

    DisplayMetrics metrics = new DisplayMetrics();
    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
    double screenWidthInDp = metrics.widthPixels;// * 9 / 10;

    Window window = getDialog().getWindow();

    window.setGravity(Gravity.CENTER_VERTICAL|Gravity.CLIP_HORIZONTAL);

    WindowManager.LayoutParams params = window.getAttributes();

    // Just an example; edit to suit your needs.
    //params.x = sourceX - dpToPx(110); // about half of confirm button size left of source view
    params.y = 250; //(metrics.heightPixels - view.getHeight()) / 2; //sourceY - dpToPx(80); // above source view
    params.width = (int) screenWidthInDp;
    params.height =  ViewGroup.LayoutParams.WRAP_CONTENT;
    //params.dimAmount = 0.8f;
    window.setAttributes(params);

    return view;
  }

//    @Override
//  public Dialog onCreateDialog(Bundle savedInstanceState) {
//    LayoutInflater i = getActivity().getLayoutInflater();
//    View view = i.inflate(R.layout.fragment_compose, null, false);
//
//    Dialog dialog = new AlertDialog.Builder(getActivity())
//            .setView(view)
//            //.setTitle(getArguments().getString(KEY_TITLE))
//
//            .setCancelable(true)
////            .setPositiveButton("OK",
////                    new DialogInterface.OnClickListener() {
////                      public void onClick(DialogInterface dialog, int whichButton) {
////                        dialog.dismiss();
////                      }
////                    }
////            )
//            .create();
//
//      final Drawable d = new ColorDrawable(Color.WHITE);
//      d.setAlpha(130);
//      dialog.getWindow().setBackgroundDrawable(d);
//
//      //dialog.getWindow().setBackgroundDrawableResource(R.drawable.);
//    return dialog;
//  }


  @Override
  public void onResume() {
    super.onResume();
    mBlurEngine.onResume(getRetainInstance());
  }

  @Override
  public void onDismiss(DialogInterface dialog) {
    super.onDismiss(dialog);
    mBlurEngine.onDismiss();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mBlurEngine.onDestroy();
  }

  @Override
  public void onDestroyView() {
    if (getDialog() != null) {
      getDialog().setDismissMessage(null);
    }
    super.onDestroyView();
  }
}
