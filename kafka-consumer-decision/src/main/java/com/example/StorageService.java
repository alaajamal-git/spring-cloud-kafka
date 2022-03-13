package com.example;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class StorageService {

	@Autowired
    private AmazonS3Client amazonS3Client;

    public boolean featchFile(String key) {
    	S3Object obj= amazonS3Client.getObject("performance-application1", "data.csv");

    	try(S3ObjectInputStream s3is = obj.getObjectContent()){
    		
    			BufferedReader in = new BufferedReader(new InputStreamReader(s3is, "UTF-8"));
    		    long result = in.lines().map(x -> x.split(";")).filter(y->Arrays.binarySearch(y, key)>=0).count();
    		    return result > 0 ? true:false;
    	}
    			catch (AmazonServiceException e) {
    		        System.err.println(e.getErrorMessage());
    		    } catch (FileNotFoundException e) {
    		        System.err.println(e.getMessage());
    		    } catch (IOException e) {
    		        System.err.println(e.getMessage());
    }
    	return false;
    
}
}