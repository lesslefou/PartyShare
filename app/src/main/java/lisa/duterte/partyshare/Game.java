package lisa.duterte.partyshare;

import android.os.Parcel;
import android.os.Parcelable;

public class Game implements Parcelable {
    private int nameId;
    private int iconId;
    private int infoId;

    public int getName() { return this.nameId; }
    public int getIconId() { return this.iconId; }
    public int getInfo() { return infoId; }

    public void setName(int n) { this.nameId = n; }
    public void setIconId(int i) { this.iconId = i; }
    public void setInfo(int i) { this.infoId = i; }

    public Game(int name, int icon, int info) {
        setName(name);
        setIconId(icon);
        setInfo(info);
    }

    public Game(Game g) {
        setName(g.nameId);
        setIconId(g.iconId);
        setInfo(g.infoId);
    }

    public Game(Parcel in) {
        this.nameId = in.readInt();
        this.iconId = in.readInt();
        this.infoId = in.readInt();
    }

    public static final Parcelable.Creator<Game> CREATOR =
            new Parcelable.Creator<Game>(){
                public Game createFromParcel(Parcel in) {
                    return new Game(in);
                }

                public Game[] newArray(int size) {
                    return new Game[size];
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
        dest.writeInt(this.infoId);
    }
}
