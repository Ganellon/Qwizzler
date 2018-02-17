/*
 * Qwizzler
 * Question.java
 * Created by Andrew Epstein
 * Copyright (c) 2018. All rights reserved.
 * Last modified : 2/15/18 5:34 PM
 */

package cloud.workingtitle.qwizzler;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


/**
 * Created by drew on 2/11/18.
 * This is the abstract Question class from which all concrete questions are derived
 */
abstract class Question implements Parcelable {

  // TODO - add a Question factory to deal with invalid constructors / empty strings
  //      TODO?: Can a factory method deal with app lifecycle?? How??!
  // TODO - add a state machine to validate questions before they are entered in the quiz
  /*
  state - initial -- question has questionText
  state - ready -- at least one correct choice was added
  state - closed -- question added to the quiz, no more modifications
   */


  // these decide what kind of controls to draw
  private Class mControlType;
  private Class mParentType;

  // the text of the question, e.g. "What is the capital of Iowa?"
  private String mQuestionText;

  // the list of possible answers to the question
  private ArrayList<Choice> mChoices = new ArrayList<>();

  // true once a question has been answered at all
  private boolean mIsAnswered;
  // true if a question was answered correctly
  private boolean mAnsweredCorrectly;

  /**
   * Constructor that accepts a Parcel, called during screen rotation / app lifecycle events
   * @param in the Parcel that was stored in the storedInstanceState bundle
   */
  Question (Parcel in) {
    mQuestionText = in.readString();
    mChoices = in.createTypedArrayList(Choice.CREATOR);
    mIsAnswered = in.readByte() != 0;
    mAnsweredCorrectly = in.readByte() != 0;

  }

  /**
   * basic constructor required for every subclass
   * @param questionText the text of the question being asked
   */
  Question(String questionText) {
    mQuestionText = questionText;
    mIsAnswered = false;
  }

  /**
   * setter
   * @param answeredCorrectly indicates whether to set the condition true or false
   */
  void setAnsweredCorrectly(boolean answeredCorrectly) {
    mAnsweredCorrectly = answeredCorrectly;
    mIsAnswered = true;
  }

  /**
   * getter
   * @return whether the question was answered correctly
   */
  boolean getAnsweredCorrectly() {
    return mAnsweredCorrectly;
  }

  /**
   * getter
   * @return whether or not the question was answered already
   */
  boolean isAnswered() {
    return mIsAnswered;
  }

  /**
   * getter
   * @return the class of the child control to display for each Choice
   */
  Class getControlType() {
    return this.mControlType;
  }

  /**
   * getter
   * @return the class of the Parent layout to create to hold child controls
   */
  Class getParentType() {
    return this.mParentType;
  }

  /**
   * setter - called by each subclass
   * @param controlType - the class of the child control to display for each Choice
   */
  void setControlType(Class controlType) {
    mControlType = controlType;
  }

  /**
   * setter - called by each subclass
   * @param parentType - the class of the parent control to display for each Choice
   */
  void setParentType(Class parentType) {
    mParentType = parentType;
  }

  /**
   * getter - the text of the question being asked
   * @return the text of the question being displayed
   */
  String getQuestionText() {
    return mQuestionText;
  }

  /**
   * OVERLOAD
   * add a new Choice object to the collection for this Question
   * @param choiceText - the text of the choice
   * silently calls the addChoice(String, boolean) constructor with a default false value
   */
  public void addChoice(String choiceText) {
    this.addChoice(choiceText, false);
  }

  /**
   * add a new Choice object to the collection for this Question
   * @param choiceText - the Text of the choice
   * @param isCorrect - indicates whether this choice is Correct to satisfy the Question
   */
  public void addChoice(String choiceText, boolean isCorrect) {
    // TODO: validate choice before calling Choice constructor
    Choice choice = new Choice(choiceText, isCorrect);
    mChoices.add(choice);
  }

  /**
   * getter - the arraylist of choices for this question
   * @return - the arraylist of choices for this question
   */
  ArrayList<Choice> getChoices() {
    return mChoices;
  }

  /**
   * required by Parcelable interface
   * @return 0
   */
  @Override
  public int describeContents() {
    return 0;
  }

  /**
   * writes the contents of this question to a Parcel for screen rotation / app lifecycle
   * @param dest the destination parcel for this serialized Question
   * @param flags flags
   */
  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mQuestionText);
    dest.writeTypedList(mChoices);
    dest.writeByte((byte) (mIsAnswered ? 1 : 0));
    dest.writeByte((byte) (mAnsweredCorrectly ? 1 : 0));
  }

  /**
   * Choice inner class
   * Each Question contains at least one Choice
   * Choices are displayed as checkboxes or RadioButtons in the QuizActivity
   */
  public static class Choice implements Parcelable {

    // the text to display for this choice
    private String mChoiceText;
    // whether this choice satisfies the Question
    private boolean mIsCorrect;
    // indicates whether this Choice was selected by the user when CheckAnswer was pressed
    // and used to preserve / display historical answers
    private boolean mWasChosen;

    /**
     * Constructor for Choice
     * @param choiceText The text to display / store for this choice
     * @param isCorrect - indicates whether this choice is correct for the Question
     */
    private Choice(String choiceText, boolean isCorrect) {
      this.mChoiceText = choiceText;
      this.mIsCorrect = isCorrect;
    }

    /**
     * OVERLOAD
     * Constructor for Choice class, inflater for previously Parceled Choices
     * @param in the Parcel stored in savedInstanceState Bundle
     */
    private Choice (Parcel in) {
      mChoiceText = in.readString();
      mIsCorrect = in.readByte() != 0;
      mWasChosen = in.readByte() != 0;
    }

    /**
     * Required for inner class of Parcelable class
     */
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

    // required by Parcelable interface
    @Override
    public int describeContents() {
      return 0;
    }

    /**
     * Writes the content of this Choice into a Parcel
     * @param dest the destination Parcel
     * @param flags flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(mChoiceText);
      dest.writeByte((byte) (mIsCorrect ? 1 : 0));
      dest.writeByte((byte) (mWasChosen ? 1 : 0));
    }

    /**
     * setter
     * Indicates that this Choice was selected by the user when the CheckAnswer button was pressed
     */
    void choose() {
      mWasChosen = true;
    }

    /**
     * getter
     * @return returns whether this Choice was historically chosen for historical display
     */
    boolean wasPreviouslyChosen() {
      return mWasChosen;
    }

    /**
     * getter
     * @return the text of this Choice which is displayed / stored for display
     */
    public String getText() {
      return mChoiceText;
    }

    /**
     * getter
     * @return whether this Choice is correct for the Question; used for marking a question correct
     */
    boolean isCorrect() {
      return mIsCorrect;
    }
  }
}
