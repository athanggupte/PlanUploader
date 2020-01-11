/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xdevapps.navigable.planuploader;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author athang213
 */
public class StorageHandler {

    public StorageHandler() throws FileNotFoundException, IOException {
        FileInputStream serviceAccount = new FileInputStream("navigable-25e2d-firebase-adminsdk-n7son-acb8463d31.json");
        
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket("navigable-25e2d.appspot.com")
                .build();
        
        FirebaseApp.initializeApp(options);
        
        Bucket bucket = StorageClient.getInstance().bucket();
        
        bucket.create("plan-1.txt", "Hello, Cloud".getBytes("UTF-8"));
    }
    
    
            
            
    
}
