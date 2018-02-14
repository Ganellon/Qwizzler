package cloud.workingtitle.qwizzler;

/**
 * Created by drew on 2/11/18.
 */

class SelectMany extends Question {

  SelectMany(String questionText) {
    super(questionText);
    setControlType(android.widget.CheckBox.class);
    setParentType(android.widget.LinearLayout.class);
  }
}
