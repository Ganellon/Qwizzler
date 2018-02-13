package cloud.workingtitle.dynamicquiz;

import java.util.ArrayList;

/**
 * Created by drew on 2/11/18.
 */

interface State {
    void addChoice();
    void setAnsweredCorrectly();

}
