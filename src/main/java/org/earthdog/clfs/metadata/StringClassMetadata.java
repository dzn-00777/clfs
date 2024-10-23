package org.earthdog.clfs.metadata;

/**
 * @Date 2024/10/17 12:47
 * @Author DZN
 * @Desc StringClassMetadata
 */
public class StringClassMetadata extends ClassMetadata {

    private byte[] byteCode;

    public StringClassMetadata(String qualifiedName, String data) {
        super(qualifiedName, data);
    }


    public byte[] getByteCode() {
        return byteCode;
    }

    public void setByteCode(byte[] byteCode) {
        this.byteCode = byteCode;
    }
}
