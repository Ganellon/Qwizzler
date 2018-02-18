
/*
 * Qwizzler
 * TrueFalse.java
 * Created by Andrew Epstein
 * Copyright (c) 2018. All rights reserved.
 * Last modified : 2/14/18 6:24 PM
 */

package cloud.workingtitle.qwizzler;

import android.os.Parcel;
import android.util.Log;

class TrueFalse extends Question {

  // let the default constructor default to true
  TrueFalse(String questionText) {
    this(questionText, true);
  }

  // call a constructor with the question and true / false answer
  TrueFalse(String questionText, boolean isCorrect) {
    super(questionText);
    setControlType(android.widget.RadioButton.class);
    setParentType(android.widget.RadioGroup.class);
    this.addChoice(isCorrect);
  }

  private TrueFalse (Parcel in) {
    super(in);
  }

  @Override
  public void addChoice(String choiceText, boolean isAnswer) {
    Log.e("TrueFalse", "addChoice method not allowed. Use the TrueFalse(String questionText, boolean answer) constructor instead");
  }

  private void addChoice(boolean isCorrect) {
    super.addChoice("True", isCorrect);
    super.addChoice("False", !isCorrect);
  }

  @Override
  public Class getControlType() {
    return super.getControlType();
  }

  @Override
  public int describeContents() {
    return 0;
  }


  public static final Creator<TrueFalse> CREATOR = new Creator<TrueFalse>() {
    @Override
    public TrueFalse createFromParcel(Parcel in) {
      return new TrueFalse(in);
    }

    @Override
    public TrueFalse[] newArray(int size) {
      return new TrueFalse[size];
    }
  };
}
