package com.example.assignment;

public class Model {
    private  String imageUri;
    private String imageName;
    public Model()
    {

    }
    public Model(String imageUri,String imageName)
    {
        if (imageName.trim().equals("")) {
            imageName = "Image"+System.currentTimeMillis();
        }
    this.imageUri=imageUri;
    this.imageName=imageName;
    }


    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
