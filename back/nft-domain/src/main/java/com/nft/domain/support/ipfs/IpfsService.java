package com.nft.domain.support.ipfs;
import java.io.IOException;
public interface IpfsService {
    /**
     * 指定path+文件名称,上传至ipfs
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    String uploadToIpfs(String filePath) throws IOException;

    /**
     * 将byte格式的数据,上传至ipfs
     *
     * @param data
     * @return
     * @throws IOException
     */
    String uploadToIpfs(byte[] data) throws IOException;

    /**
     * 根据Hash值,从ipfs下载内容,返回byte数据格式
     *
     * @param hash
     * @return
     */
    byte[] downFromIpfs(String hash);

    /**
     * 根据Hash值,从ipfs下载内容,并写入指定文件destFilePath
     *
     * @param hash
     * @param destFilePath
     */
    void downFromIpfs(String hash, String destFilePath);

    /**
     * @Des 封装一下让接口直接传入简单参数即可使用ipfs存贮
     * @Date 2023/12/13 17:27
     * @Param id 提交表中id 用于获取存贮路径
     * @Return hash 返回ipfs中存贮的hash
     */
    String addIpfsById(String id) ;
}
