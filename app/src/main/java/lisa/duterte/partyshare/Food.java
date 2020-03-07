package lisa.duterte.partyshare;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private int nameId;
    private int iconId;
    private int quantityId;

    public int getName() { return this.nameId; }
    public int getIconId() { return this.iconId; }
    public int getQuantity() { return quantityId; }

    public void setName(int n) { this.nameId = n; }
    public void setIconId(int i) { this.iconId = i; }
    public void setQuantity(int i) { this.quantityId = i; }

    public Food(int name, int icon, int quantity) {
        setName(name);
        setIconId(icon);
        setQuantity(quantity);
    }

    public Food(Food f) {
        setName(f.nameId);
        setIconId(f.iconId);
        setQuantity(f.quantityId);
    }

    public Food(Parcel in) {
        this.nameId = in.readInt();
        this.iconId = in.readInt();
        this.quantityId = in.readInt();
    }

    public static final Parcelable.Creator<Food> CREATOR =
            new Parcelable.Creator<Food>(){
                public Food createFromParcel(Parcel in) {
                    return new Food(in);
                }

                public Food[] newArray(int size) {
                    return new Food[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.nameId);
        dest.writeInt(this.iconId);
        dest.writeInt(this.quantityId);
    }
}
