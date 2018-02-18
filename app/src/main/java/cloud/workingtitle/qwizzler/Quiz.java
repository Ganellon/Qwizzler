package cloud.workingtitle.qwizzler;

import java.util.ArrayList;

/**
 * Created by Andrew Epstein on 2/16/18.
 * This is a composition class so that Question state can be validated prior to being added
 * to the ArrayList. Previously, the ArrayList was an instance variable of the Activity class
 * and had no means of validating input.
 *
 * This does not prevent someone from destroying the question bank or circumventing the static
 * methods by using getAll and then calling ArrayList methods directly. Just prevents silly mistakes.
 *
 */

abstract class Quiz {

  // bank of questions
  private static ArrayList<Question> mQuestions = new ArrayList<>();

  static boolean add(Question question) {
    // <do some validation processing for each question state>
    mQuestions.add(question);
    return true;
  }

  /**
   * this is a kludge; not sure how to deal with static data in the app lifecycle
   */
  static void destroy() {
    mQuestions.clear();
  }

  static Question get(int index) {
    return mQuestions.get(index);
  }

  static ArrayList<Question> getAll() {
    return mQuestions;
  }

  static int size() {
    return mQuestions.size();
  }

  static void setAll(ArrayList<Question> questions) {
    mQuestions = questions;
  }
}
