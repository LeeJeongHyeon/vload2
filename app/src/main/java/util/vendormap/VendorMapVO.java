package util.vendormap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;

import util.ImageConverter;
import util.category.LogTag;
import vmp.company.vexmall.R;
public class VendorMapVO implements android.os.Parcelable, Serializable {
    private final String TAG = LogTag.getTag(this);
    private static Context context;
    private String companyName;
    private String industry;
    private String postCode;
    private String addr1;
    private String addr2;
    private String imagePath;
    private String vm;
    private double longitude;
    private double latitude;
    private Bitmap bitmap;

    private VendorMapVO(Parcel parcel) {
        readFromParcel(parcel);
    }

    public VendorMapVO(Context context, String companyName, String industry, String postCode, String addr1, String addr2, String imagePath, String vm, double longitude, double latitude){
        this(companyName, industry, postCode, addr1, addr2, imagePath, vm, longitude, latitude);
        this.context = context;

    }

    public VendorMapVO(String companyName, String industry, String postCode, String addr1, String addr2, String imagePath, String vm, double longitude, double latitude) {
        this.companyName = companyName;
        this.industry = industry;
        this.postCode = postCode;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.imagePath = imagePath;
        this.vm = vm;
        this.longitude = longitude;
        this.latitude = latitude;
        if(imagePath == null || imagePath.isEmpty()) {
            this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
        }  else {
            this.bitmap = ImageConverter.getInstance(context).uriToBitmap(Uri.parse(imagePath),true);
            Log.d(TAG, "new Bitmap created.");
        }
    }
    public String getCompanyName() {
        return companyName;
    }

    public String getIndustry() {
        return industry;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getAddr1() {
        return addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getVm() {
        return vm;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        VendorMapVO.context = context;
    }

    @Override
    public String toString() {
        return "VendorMapVO{" +
                "companyName='" + companyName + '\'' +
                ", industry='" + industry + '\'' +
                ", postCode='" + postCode + '\'' +
                ", addr1='" + addr1 + '\'' +
                ", addr2='" + addr2 + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", vm='" + vm + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", bitmap=" + bitmap +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(context);
        parcel.writeString(companyName);
        parcel.writeString(industry);
        parcel.writeString(postCode);
        parcel.writeString(addr1);
        parcel.writeString(addr2);
        parcel.writeString(imagePath);
        parcel.writeString(vm);
        parcel.writeDouble(longitude);
        parcel.writeDouble(latitude);
        parcel.writeValue(bitmap);
    }


    private void readFromParcel(Parcel parcel) {
        this.context = parcel.readParcelable(Context.class.getClassLoader());
        this.companyName = parcel.readString();
        this.industry = parcel.readString();
        this.postCode = parcel.readString();
        this.addr1 = parcel.readString();
        this.addr2 = parcel.readString();
        this.imagePath = parcel.readString();
        this.vm = parcel.readString();
        this.longitude = parcel.readDouble();
        this.latitude = parcel.readDouble();
        this.bitmap = parcel.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public VendorMapVO createFromParcel(Parcel src) {
            return new VendorMapVO(src);
        }

        public VendorMapVO[] newArray(int size) {
            return new VendorMapVO[size];
        }
    };
}
