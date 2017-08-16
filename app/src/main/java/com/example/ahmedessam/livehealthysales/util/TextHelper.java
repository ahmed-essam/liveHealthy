package com.example.ahmedessam.livehealthysales.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.ahmedessam.livehealthysales.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHelper {

    //Checker for Email format
    public  static boolean isEmail(EditText editText) {

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = editText.getText().toString().trim();

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            return true;
        }

        editText.requestFocus();
        editText.setError("البريد الالكتروني غير صحيح");
        return false;
    }
    //Checker for Password format
    public  static boolean isPassword(EditText editText){

        String s = editText.getText().toString();
        if(editText.getText().toString().trim().length() < 4 || s.contains(" ")){
            editText.setText("");
            editText.requestFocus();
            editText.setError("كلمة السر يجب ان لا تقل عن 4 أحرف");
            return false;
        }
        return true;
    }
    //Checker for Password confirmation
    public static boolean isCarbonCopy(EditText editText1, EditText editText2){

        if (!editText1.getText().toString().trim()
                .equals(editText2.getText().toString().trim())){
            editText2.requestFocus();
            editText2.setError("كلمات السر غير متطابقة");
            return false;
        }
        return true;
    }
    //Checker for empty fields
    public static boolean isEditTextEmpty(EditText editText[],Context context){

        for (EditText anEditText : editText) {

            if (TextUtils.isEmpty(anEditText.getText().toString().trim())) {

                anEditText.requestFocus();
                anEditText.setError(context.getResources().getString(R.string.fill_data));
                clearEditText(new EditText[]{anEditText});
                return true;
            }
        }
        return false;
    }
    //Clears all passed Edit Texts
    public static void clearEditText(EditText editText[]){

        for (byte i = 0; i < editText.length; i++){

            editText[i].setText("");
        }
    }
    //hide keyboard
    public static void hideKeyboard(Context context){

        View view = ((Activity)context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }    }
}
