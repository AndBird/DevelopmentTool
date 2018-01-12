package com.android_development.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/4/12.
 * ����: MD5 У�飬����md5��
 */
public class MD5Tool {
        public static String getMD5(String instr) {
            String s = null;

            char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f' };
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(instr.getBytes());
                byte tmp[] = md.digest();

                char str[] = new char[16 * 2];

                int k = 0;
                for (int i = 0; i < 16; i++) {
                    byte byte0 = tmp[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                s = new String(str);
            } catch (Exception e) {

            }
            return s;
        }
        
        
        public static String getMD5(byte[] instr) {
    		String s = null;
    		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    				'a', 'b', 'c', 'd', 'e', 'f' };
    		try {
    			MessageDigest md = MessageDigest.getInstance("MD5");
    			md.update(instr);
    			byte tmp[] = md.digest();
    			char str[] = new char[16 * 2];
    			int k = 0;
    			for (int i = 0; i < 16; i++) {
    				byte byte0 = tmp[i];
    				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
    				str[k++] = hexDigits[byte0 & 0xf];
    			}
    			s = new String(str);
    		} catch (Exception e) {

    		}
    		return s;
    	}

        /**
         * ��ȡ�ļ���md5
         * 
         * С�ļ����ã����ļ��Ƚ���
         * */
        public static String getFileMD5(String filename){
            String s = null;
            char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
            FileInputStream fis = null;
            try{
                MessageDigest md = MessageDigest.getInstance("MD5");
                fis = new FileInputStream(filename);
                //byte[] buffer = new byte[2048];//С�ļ�
                byte[] buffer = new byte[2048 * 40];//��2048�죬����̫����Ҳ��
                //byte[] buffer = new byte[2048 * 50];//��2048�죬����̫����Ҳ��
                int length = -1;
                while((length = fis.read(buffer)) != -1){
                    md.update(buffer, 0, length);
                }
                byte []b = md.digest();
                char str[] = new char[16 * 2];

                int k = 0;
                for (int i = 0; i < 16; i++) {

                    byte byte0 = b[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                s = new String(str);
                return s;
            }catch(Exception e){
                return null;
            }finally{
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
        /**
         * ��ȡ�ļ���sha1
         * 
         * С�ļ����ã����ļ��Ƚ���
         * */
        public static String getFileSHA1(String filename){
            String s = null;
            char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','A', 'B', 'C', 'D', 'E', 'F' };
            FileInputStream fis = null;
            try{
                MessageDigest md = MessageDigest.getInstance("SHA1");
                fis = new FileInputStream(filename);
                //byte[] buffer = new byte[2048];//С�ļ�
                byte[] buffer = new byte[2048 * 40];//��2048�죬����̫����Ҳ��
                //byte[] buffer = new byte[2048 * 50];//��2048�죬����̫����Ҳ��
                int length = -1;
                while((length = fis.read(buffer)) != -1){
                    md.update(buffer, 0, length);
                }
                byte []b = md.digest();
                char str[] = new char[16 * 2];

                int k = 0;
                for (int i = 0; i < 16; i++) {

                    byte byte0 = b[i];
                    str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                    str[k++] = hexDigits[byte0 & 0xf];
                }
                s = new String(str);
                return s;
            }catch(Exception e){
                return null;
            }finally{
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        
    	/**
    	 * �Զ���ļ�����ļ�md5,�ٶȽϿ�
    	 * ע:�˷����������̨����һ�£��ſ�У��
    	 * 
    	 * */
    	private static String getBigFileMd5(final String filePath){
    		String PRIV_KEY = "123456789";
    		int FILE_SIZE_100MB =  104857600;
    		int FILE_SIZE_50MB = 52428800;
    		String ret = null;
    		try {
    			String md5Code;
    			long filesize = 0;
    			//�ж��ļ��Ĵ�СȻ���ǰ���ȡ30 ��bytes ����MD5������
    			File file = new File(filePath);
    			if (file.exists()){
    				RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
    				String chunkMd5 = getMD5(PRIV_KEY);
    				if(randomAccessFile != null){
    					filesize = randomAccessFile.length();
    					//�����ļ����ȵ�MD5
    					String sMd5 = String.valueOf(filesize);
    					sMd5 = getMD5(sMd5);

    					if(filesize <= 630){//��ȡ�������ļ�MD5
    						if(randomAccessFile != null){
    							randomAccessFile.close();
    							randomAccessFile = null;
    						}
    						md5Code = getFileMD5(filePath);
    						ret = md5Code;	
    						sMd5 = sMd5 + md5Code;
    					}else{
    						//��ȡ�̶��ĸ�bytes���м���
    						byte[] filedata = new byte[60];
    						randomAccessFile.seek(300);
    						randomAccessFile.read(filedata, 0, 30);
    						randomAccessFile.seek(filesize - 330);
    						randomAccessFile.read(filedata, 30, 30);
    						md5Code = getMD5(filedata);
    						sMd5 = sMd5 + md5Code;
    						if (filesize >= FILE_SIZE_100MB){
    							long fileChunksize = filesize;
    							long fileoffset = FILE_SIZE_50MB;

    							while (fileChunksize > FILE_SIZE_100MB){
    								randomAccessFile.seek(fileoffset);
    								byte[] tmpByte = new byte[64];
    								byte[] dataByte = chunkMd5.getBytes();
    								//DebugUtils.printInfo(TAG, "file md5 dataByte length=" + dataByte.length);
    								System.arraycopy(dataByte, 0, tmpByte, 0, dataByte.length);
    								randomAccessFile.read(tmpByte, dataByte.length, 32);

    								chunkMd5 = getMD5(tmpByte);
    								fileChunksize -= FILE_SIZE_100MB;
    								fileoffset += FILE_SIZE_100MB;
    							}
    						}
    						if(randomAccessFile != null){
    							randomAccessFile.close();
    							randomAccessFile = null;
    						}
    					}
    					sMd5 = sMd5 + chunkMd5;
    					ret = getMD5(sMd5);
    				}
    			}
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		return ret;
    	}  
    	
    	/*
    	 * �ο�php
    	 * 
    	 * GetBigFileMD5Code(const string& filePath)
    	{
	    #define PRIV_KEY "123456789"
	    #define FILE_SIZE_100MB 104857600
	    #define FILE_SIZE_50MB 52428800
    		string ret;
    		char md5Code[33] = {0};
    		int filesize = 0;
    		//�ж��ļ��Ĵ�СȻ���ǰ���ȡ30 ��bytes ����MD5������
    		if (PathFileExists(filePath.c_str()))
    		{
    			FILE* fp = fopen(filePath.c_str(), "rb");
    			string chunkMd5 = PRIV_KEY;
    			RootDownloader::GetStringMD5((BYTE*)chunkMd5.c_str(), chunkMd5.size(), (BYTE*)md5Code);
    			chunkMd5 = md5Code;
    			if (fp)
    			{
    				fseek(fp, 0, SEEK_END);
    				filesize = ftell(fp);
    				rewind(fp);

    				//�����ļ����ȵ�MD5
    				char filesizeMd5[33] ={0};
    				string sMd5 = StringConverter::toString(filesize);
    				RootDownloader::GetStringMD5((BYTE*)sMd5.c_str(), sMd5.size(), (BYTE*)filesizeMd5);
    				sMd5 = filesizeMd5;

    				if (filesize <= 630)		//��ȡ�������ļ�MD5
    				{
    					fclose(fp);
    					if(RootDownloader::GetFileMd5(filePath.c_str(), (BYTE*)md5Code))
    					{
    						ret = md5Code;	
    						sMd5 = sMd5 + md5Code;
    					}else{
    						G_LOG("��ȡ�ļ�MD5��ʧ��!file:%s", filePath.c_str());
    					}
    				}else{
    					//��ȡ�̶��ĸ�bytes���м���
    					char filedata[60] = {0};
    					fseek(fp, 300, SEEK_SET);
    					fread(filedata, 30, 1, fp);
    					fseek(fp, filesize - 330, SEEK_SET);
    					fread(filedata+30, 30, 1, fp);
    					//����md5
    					if (RootDownloader::GetStringMD5((BYTE*)filedata, 60, (BYTE*)md5Code))
    					{
    						sMd5 = sMd5 + md5Code;
    					}else{
    						G_LOG("��ȡ�ļ�MD5��ʧ��!file:%s", filePath.c_str());
    					}


    					if (filesize >= FILE_SIZE_100MB)
    					{
    						int fileChunksize = filesize;
    						unsigned int fileoffset = FILE_SIZE_50MB;
    						char chunkCode[33] = {0};

    						while (fileChunksize>FILE_SIZE_100MB)
    						{
    							fseek(fp, fileoffset, SEEK_SET);
    							char tmpdata[65] = {0};
    							memcpy(tmpdata, chunkMd5.c_str(), chunkMd5.size());
    							fread(tmpdata + chunkMd5.size(), 1, 32, fp);
    							//��������MD5
    							//string sChunk = chunkMd5 + tmpdata;

    							RootDownloader::GetStringMD5((BYTE*)tmpdata, 64, (BYTE*)chunkCode);
    							chunkMd5 = chunkCode;
    							fileChunksize -= FILE_SIZE_100MB;
    							fileoffset += FILE_SIZE_100MB;
    						}
    					}
    					fclose(fp);
    				}
    				sMd5 = sMd5 + chunkMd5;
    				RootDownloader::GetStringMD5((BYTE*)sMd5.c_str(), sMd5.size(), (BYTE*)md5Code);
    				ret = md5Code;


    			}
    		}
    		return ret;
    	}*/

}
