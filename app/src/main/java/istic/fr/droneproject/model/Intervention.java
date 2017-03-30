package istic.fr.droneproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Modèle d'une intervention
 */
public class Intervention implements Parcelable {
    public String _id;
    public String libelle;
    public String adresse;
    public Double[] position;
    public String date;
    public CodeSinistre code;
    public List<Vehicule> vehicules;
    public List<PointInteret> points;
    public List<ParcoursSegment> segments;
    public List<ParcoursZone> zones;
    public List<Photo> photos;
    public boolean cloturer;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this._id);
        dest.writeString(this.libelle);
        dest.writeString(this.adresse);
        dest.writeArray(this.position);
        dest.writeString(this.date);
        dest.writeInt(this.code == null ? -1 : this.code.ordinal());
        dest.writeList(this.vehicules);
        dest.writeList(this.points);
        dest.writeList(this.segments);
        dest.writeList(this.zones);
        dest.writeList(this.photos);
        dest.writeByte(this.cloturer ? (byte) 1 : (byte) 0);
    }

    public Intervention() {
    }

    protected Intervention(Parcel in) {
        this._id = in.readString();
        this.libelle = in.readString();
        this.adresse = in.readString();
        this.position = (Double[]) in.readArray(Double[].class.getClassLoader());
        this.date = in.readString();
        int tmpCode = in.readInt();
        this.code = tmpCode == -1 ? null : CodeSinistre.values()[tmpCode];
        this.vehicules = new ArrayList<>();
        in.readList(this.vehicules, Vehicule.class.getClassLoader());
        this.points = new ArrayList<>();
        in.readList(this.points, PointInteret.class.getClassLoader());
        this.segments = new ArrayList<>();
        in.readList(this.segments, ParcoursSegment.class.getClassLoader());
        this.zones = new ArrayList<>();
        in.readList(this.zones, ParcoursZone.class.getClassLoader());
        this.photos = new ArrayList<>();
        in.readList(this.photos, Photo.class.getClassLoader());
        this.cloturer = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Intervention> CREATOR = new Parcelable.Creator<Intervention>() {
        @Override
        public Intervention createFromParcel(Parcel source) {
            return new Intervention(source);
        }

        @Override
        public Intervention[] newArray(int size) {
            return new Intervention[size];
        }
    };
}
