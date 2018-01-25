package com.example.szopen.elsreader;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.util.ASN1Dump;


import java.io.IOException;

import static android.content.ContentValues.TAG;

public class ELSReader implements NfcAdapter.ReaderCallback {
    private Context context = null;
    private ELSViewer parent = null;
    public NfcAdapter nfcAdapter = null;
    private IsoDep cardHolder = null;
    private Tag lastTag = null;

    ELSReader(Context c, ELSViewer elsViewer) {
        context = c;
        parent = elsViewer;
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        cardHolder = IsoDep.get(tag);
        lastTag = tag;
        parent.onCardConnected();
    }

    public String startELS() throws IOException {
        String response = this.selectFile("00A4040007D616000030010100");
        Log.d(TAG, "startELS: " + response);
        return response;
    }

    public String selectEFELS() throws IOException {
        if (this.startELS().equals("9000")) {
            String response = this.selectFile("00A4000002000200");
            Log.d(TAG, "startELS: " + response);
            return response;
        }
        return "ERROR";
    }

    public StudentInfo readEFELS() throws IOException {
        StudentInfo studentInfo = new StudentInfo();
        String readCommand = "00B0";
        if (this.selectEFELS().equals("9000")) {
            String file = "";
            int position = 0;
            while (position < 944) {
                String response = this.readBinary(readCommand + ByteHelper.intToHexString(position) + "20");
                response = response.substring(0, response.length() - 4);
                file += response;
                position = position + 32;
            }


            ASN1Object seqObject = new ASN1InputStream(ByteHelper.hexStringToByteArray(file)).readObject();
            Log.d(TAG, "readEFELS: " + seqObject.getClass().toString());
            if (seqObject instanceof DLSequence) {
                DERTaggedObject obj1 = (DERTaggedObject) (((DLSequence) seqObject).getObjectAt(1));
                DERSequence obj2 = (DERSequence) obj1.getObject();
                DERSequence obj3 = (DERSequence) obj2.getObjectAt(2);
                DERTaggedObject obj4 = (DERTaggedObject) (obj3.getObjectAt(1));
                DEROctetString obj5 = (DEROctetString) obj4.getObject();
                ASN1Object obj6 = new ASN1InputStream(obj5.getOctets()).readObject();
                DLSequence finalObject = (DLSequence) obj6;



                studentInfo.university = finalObject.getObjectAt(2).toString();

                DLSequence surname = (DLSequence) finalObject.getObjectAt(3);
                studentInfo.surname = surname.getObjectAt(0).toString();

                DLSequence names = (DLSequence) finalObject.getObjectAt(4);
                studentInfo.names = names.getObjectAt(0).toString();

                studentInfo.pesel = finalObject.getObjectAt(7).toString();
                studentInfo.index = finalObject.getObjectAt(5).toString();

                studentInfo.print();

                Log.d(TAG, "readEFELS: asdf" +studentInfo.names);
            }
            return studentInfo;
        }
        return null;
    }

    private String selectFile(String command) throws IOException {
        byte[] apdu = ByteHelper.hexStringToByteArray(command);

        if (cardHolder != null)
            try {
                cardHolder.connect();
                final byte[] response = cardHolder.transceive(apdu);
                String str = ByteHelper.byteArrayToHex(response);
                cardHolder.close();
                return str.substring(str.length() - 4);

            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        return "";
    }

    private String readBinary(String command) throws IOException {
        byte[] apdu = ByteHelper.hexStringToByteArray(command);

        if (cardHolder != null)
            try {
                cardHolder.connect();
                final byte[] response = cardHolder.transceive(apdu);
                String str = ByteHelper.byteArrayToHex(response);
                cardHolder.close();
                return str;

            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        return "";
    }


    public String getMifareSN() {
        if (lastTag != null) return ByteHelper.byteArrayToHex(lastTag.getId());
        else return ByteHelper.byteArrayToHex(new byte[0]);
    }
}
