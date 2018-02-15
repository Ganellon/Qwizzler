
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

class SelectMany extends Question implements Parcelable{

  SelectMany(String questionText) {
    super(questionText);
    setControlType(android.widget.CheckBox.class);
    setParentType(android.widget.LinearLayout.class);
  }

  private SelectMany(Parcel in) {
    super(in);
  }

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
  }
}
