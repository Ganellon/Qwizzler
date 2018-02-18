
/*
 * Qwizzler
 * SelectMany.java
 * Created by Andrew Epstein
 * Copyright (c) 2018. All rights reserved.
 * Last modified : 2/14/18 6:24 PM
 */

package cloud.workingtitle.qwizzler;

import android.os.Parcel;
import android.os.Parcelable;

class SelectMany extends Question implements Parcelable {

  /**
   * Constructor
   * @param questionText the text of the question to be displayed
   */
  SelectMany(String questionText) {
    super(questionText);
    setControlType(android.widget.CheckBox.class);
    setParentType(android.widget.LinearLayout.class);
  }

  /**
   * Constructor for the Parcelable interface
   * @param in the Parcel previously stored in instanceState Bundle
   */
  private SelectMany(Parcel in) {
    super(in);
  }

  /**
   * Parcelable interface
   */
  public static final Creator<SelectMany> CREATOR = new Creator<SelectMany>() {
    @Override
    public SelectMany createFromParcel(Parcel in) {
      return new SelectMany(in);
    }

    @Override
    public SelectMany[] newArray(int size) {
      return new SelectMany[size];
    }
  };

  /**
   * Parcelable interface
   * @return 0
   */
  @Override
  public int describeContents() {
    return 0;
  }

}
