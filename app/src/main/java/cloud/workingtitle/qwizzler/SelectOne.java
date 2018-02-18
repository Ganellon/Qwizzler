
/*
 * Qwizzler
 * SelectOne.java
 * Created by Andrew Epstein
 * Copyright (c) 2018. All rights reserved.
 * Last modified : 2/14/18 6:24 PM
 */

package cloud.workingtitle.qwizzler;

import android.os.Parcel;
import android.os.Parcelable;


class SelectOne extends Question implements Parcelable{

  SelectOne(String questionText) {
    super(questionText);
    setControlType(android.widget.RadioButton.class);
    setParentType(android.widget.RadioGroup.class);
  }

  private SelectOne(Parcel in) {
    super(in);
  }

  public static final Creator<SelectOne> CREATOR = new Creator<SelectOne>() {
    @Override
    public SelectOne createFromParcel(Parcel in) {
      return new SelectOne(in);
    }

    @Override
    public SelectOne[] newArray(int size) {
      return new SelectOne[size];
    }
  };

  @Override
  public int describeContents() {
    return 0;
  }
}
