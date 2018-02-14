package cloud.workingtitle.qwizzler;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by drew on 2/11/18.
 */

class SelectMany extends Question implements Parcelable{

  SelectMany(String questionText) {
    super(questionText);
    setControlType(android.widget.CheckBox.class);
    setParentType(android.widget.LinearLayout.class);
  }

  protected SelectMany(Parcel in) {
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
