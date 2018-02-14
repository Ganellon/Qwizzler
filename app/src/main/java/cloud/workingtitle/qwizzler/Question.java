package cloud.workingtitle.qwizzler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by drew on 2/11/18.
 * This is the abstract Question class from which all concrete questions are derived
 */

abstract class Question implements Parcelable {

  // Question can have several states, the least of which is basic question text
  // Question has at least one choice
  // Question has at least one correct choice
  // For SelectOne and

  // these decide what kind of controls to draw
  private Class mControlType;
  private Class mParentType;

  // the text of the question, e.g. "What is the capital of Iowa?"
  private String mQuestionText;

  // the list of possible answers to the question
  private ArrayList<Choice> mChoices = new ArrayList<>();

  private boolean mIsAnswered;
  private boolean mAnsweredCorrectly;


  Question (Parcel in) {
    mQuestionText = in.readString();
    mChoices = in.createTypedArrayList(Choice.CREATOR);
    mIsAnswered = in.readByte() != 0;
    mAnsweredCorrectly = in.readByte() != 0;
  }

  Question(String questionText) {
    mQuestionText = questionText;
    mIsAnswered = false;
  }

  /*public static final Creator<Question> CREATOR = new Creator<Question>() {
    @Override
    public Question createFromParcel(Parcel in) {
      return new Question(in);
    }

    @Override
    public Question[] newArray(int size) {
      return new Question[size];
    }
  };*/

  void setAnsweredCorrectly(boolean answeredCorrectly) {
    mAnsweredCorrectly = answeredCorrectly;
    mIsAnswered = true;
  }

  boolean getAnsweredCorrectly() {
    return mAnsweredCorrectly;
  }

  boolean isAnswered() {
    return mIsAnswered;
  }

  Class getControlType() {
    return this.mControlType;
  }

  Class getParentType() {
    return this.mParentType;
  }

  void setControlType(Class controlType) {
    mControlType = controlType;
  }

  void setParentType(Class parentType) {
    mParentType = parentType;
  }

  String getQuestionText() {
    return mQuestionText;
  }

  public void addChoice(String choiceText) {
    this.addChoice(choiceText, false);
  }

  public void addChoice(String choiceText, boolean isCorrect) {
    Choice choice = new Choice(choiceText, isCorrect);
    mChoices.add(choice);
  }

  ArrayList<Choice> getChoices() {
    return mChoices;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mQuestionText);
    dest.writeTypedList(mChoices);
    dest.writeByte((byte) (mIsAnswered ? 1 : 0));
    dest.writeByte((byte) (mAnsweredCorrectly ? 1 : 0));
  }

  public static class Choice implements Parcelable{
    private String mChoiceText;
    private boolean mIsCorrect;
    private boolean mWasChosen;

    private Choice(String choiceText, boolean isCorrect) {
      this.mChoiceText = choiceText;
      this.mIsCorrect = isCorrect;
    }

    private Choice (Parcel in) {
      mChoiceText = in.readString();
      mIsCorrect = in.readByte() != 0;
      mWasChosen = in.readByte() != 0;
    }


    void choose() {
      mWasChosen = true;
    }

    boolean wasPreviouslyChosen() {
      return mWasChosen;
    }

    public String getText() {
      return mChoiceText;
    }

    boolean isCorrect() {
      return mIsCorrect;
    }

    static final Creator<Choice> CREATOR = new Creator<Choice>() {
      @Override
      public Choice createFromParcel(Parcel in) {
        return new Choice(in);
      }

      @Override
      public Choice[] newArray(int size) {
        return new Choice[size];
      }
    };

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(mChoiceText);
      dest.writeByte((byte) (mIsCorrect ? 1 : 0));
      dest.writeByte((byte) (mWasChosen ? 1 : 0));
    }

  }
}
