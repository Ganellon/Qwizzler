package cloud.workingtitle.dynamicquiz;

/**
 * Created by drew on 2/11/18.
 */

class SelectMany extends Question {

    SelectMany(String questionText) {
        super(questionText);
        super.setControlType(android.widget.CheckBox.class);
        super.setParentType(android.widget.LinearLayout.class);
    }
}
