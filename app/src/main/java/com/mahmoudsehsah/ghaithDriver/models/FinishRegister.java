package com.mahmoudsehsah.ghaithDriver.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mahmoud on 3/25/2018.
 */

public class FinishRegister {

    @SerializedName("file")
    @Expose
    private String file;

    @SerializedName("file2")
    @Expose
    private String file2;

    @SerializedName("file3")
    @Expose
    private String file3;

    @SerializedName("file4")
    @Expose
    private String file4;


    public String getfile() {
        return file;
    }
    public void setfile(String file) {
        this.file = file;
    }


    public String getfile2() {
        return file2;
    }
    public void setfile2(String file2) {
        this.file2 = file2;
    }


    public String getfile3() {
        return file3;
    }
    public void setattached3(String file3) {
        this.file3 = file3;
    }


    public String getfile4() {
        return file4;
    }
    public void setfile4(String file4) {
        this.file4 = file4;
    }
}
