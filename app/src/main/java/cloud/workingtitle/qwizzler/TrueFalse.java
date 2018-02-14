package cloud.workingtitle.qwizzler;

import android.util.Log;

/**
 * Created by drew on 2/11/18.
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

}
