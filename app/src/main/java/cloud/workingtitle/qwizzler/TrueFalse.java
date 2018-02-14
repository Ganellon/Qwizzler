package cloud.workingtitle.qwizzler;

import android.os.Parcel;
import android.util.Log;

/**
 * Created by drew on 2/11/18.
 * This is a TrueFalse type
 */

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
  public void addChoice(String choiceText, boolean isAnswer) throws IllegalStateException {
    Log.e(this.toString(), "Use the TrueFalse(String questionText, boolean answer) constructor instead");
    throw new IllegalStateException("addChoice is not supported for TrueFalse questions");
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

  @Override
  public void writeToParcel(Parcel dest, int flags) {

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
