
/*
 * Qwizzler
 * ShortAnswer.java
 * Created by Andrew Epstein
 * Copyright (c) 2018. All rights reserved.
 * Last modified : 2/15/18 5:08 PM
 */

package cloud.workingtitle.qwizzler;

import android.os.Parcel;

class ShortAnswer extends Question {

  private String mProvidedAnswer;

  private ShortAnswer (Parcel in) {
    super(in);
    mProvidedAnswer = in.readString();
  }


  ShortAnswer(String questionText) {
    super(questionText);
    setControlType(android.widget.EditText.class);
    setParentType(android.widget.LinearLayout.class);
    //setControlType(android.support.design.widget.TextInputEditText.class);
    //setParentType(android.support.design.widget.TextInputLayout.class);
  }

  @Override
  public void addChoice(String choiceText) {
    super.addChoice(choiceText, true);
  }

  void setAnsweredCorrectly(boolean answeredCorrectly, String textAnswer) {
    super.setAnsweredCorrectly(answeredCorrectly);
    this.mProvidedAnswer = textAnswer;
  }

  String getProvidedAnswer() {
    return mProvidedAnswer;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mProvidedAnswer);
  }

  public static final Creator<ShortAnswer> CREATOR = new Creator<ShortAnswer>() {
    @Override
    public ShortAnswer createFromParcel(Parcel in) {
      return new ShortAnswer(in);
    }

    @Override
    public ShortAnswer[] newArray(int size) {
      return new ShortAnswer[size];
    }
  };
}
