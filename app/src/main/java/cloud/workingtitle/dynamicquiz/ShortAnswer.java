package cloud.workingtitle.dynamicquiz;

/**
 * Created by drew on 2/11/18.
 */

class ShortAnswer extends Question {

  private String mProvidedAnswer;

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

  public void setAnsweredCorrectly(boolean answeredCorrectly, String textAnswer) {
    super.setAnsweredCorrectly(answeredCorrectly);
    this.mProvidedAnswer = textAnswer;
  }

  public String getProvidedAnswer(){
    return mProvidedAnswer;
  }

}
