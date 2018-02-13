package cloud.workingtitle.dynamicquiz;

/**
 * Created by drew on 2/6/18.
 */

class SelectOne extends Question {

    SelectOne(String questionText) {
        super(questionText);
        super.setControlType(android.widget.RadioButton.class);
        super.setParentType(android.widget.RadioGroup.class);
    }
}
