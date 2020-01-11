/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xdevapps.navigable.planuploader;

import com.google.api.core.ApiFuture;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.Bucket;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.cloud.StorageClient;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author athang213
 */
public class StorageHandler {

    private Bucket bucket;
    private Firestore db;
    
    public StorageHandler() throws FileNotFoundException, IOException {
        FileInputStream serviceAccount = new FileInputStream("navigable-25e2d-firebase-adminsdk-n7son-4ac20a54a4.json");
        
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setProjectId("navigable-25e2d")
                .setStorageBucket("navigable-25e2d.appspot.com")
                .build();
        
        FirebaseApp.initializeApp(options);
        
        bucket = StorageClient.getInstance().bucket();
       
        db = FirestoreClient.getFirestore();
        
        
    }
    
    public void uploadFile(byte[] bytes, int size) throws UnsupportedEncodingException {
        
//        Blob blob = bucket.create("plan-1.txt", "Hello, Cloud".getBytes("UTF-8"));
        Blob blob = bucket.create("plan-1.txt", bytes, "text/plain");
        
        DocumentReference doc = db.collection("plans").document("RahulRaj");
        Map<String, Object> data = new HashMap<>();
        data.put("Name", "Rahul Raj Mall");
        data.put("URL", "plan-1.txt");
        
        ApiFuture<WriteResult> result = doc.set(data);
        /*System.out.println("Update time: " + result.get().getUpdateTime());*/
        
        System.out.println("blob self link:" + blob.getSelfLink());
        System.out.println("blob media link:" + blob.getMediaLink());
        
        Page<Blob> blobs = bucket.list();
        for (Blob _blob : blobs.iterateAll()) {
            System.out.println("Blob:"+_blob.getName());
        }
    }
    
}
