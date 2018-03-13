package com.mahmoudsehsah.ghaithDriver.Server;

/**
 * Created by mahmoud on 2/9/2018.
 */

public class Model {

    private String rom;
    private String screenSize;
    private String backCamera;

    // getter / setter

    public String getRom(){return  rom; }
    public void setRom(String mrom){ this.rom = mrom;}

    public String getScreenSize(){ return  screenSize; }
    public void setScreenSize(String mscreensize){ this.screenSize = mscreensize; }

    public String getBackCamera(){ return  backCamera;}
    public void setBackCamera(String mBackcamera){ this.backCamera = mBackcamera; }
}