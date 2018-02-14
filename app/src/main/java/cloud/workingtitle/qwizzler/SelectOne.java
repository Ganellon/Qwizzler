package cloud.workingtitle.qwizzler;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by drew on 2/6/18.
 */

class SelectOne extends Question implements Parcelable{

  SelectOne(String questionText) {
    super(questionText);
    setControlType(android.widget.RadioButton.class);
    setParentType(android.widget.RadioGroup.class);
  }


  protected SelectOne(Parcel in) {
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

  @Override
  public void writeToParcel(Parcel dest, int flags) {
  }
}
