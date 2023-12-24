package com.nft.infrastructure.repository;

import com.nft.domain.support.ipfs.IpfsService;
import com.nft.infrastructure.dao.SubmitCacheMapper;
import com.nft.infrastructure.po.SubmitCache;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
* @author: 戏人看戏
* @Date: 2023/12/12 14:16
* @Description: IPFS 方法类
*/
@Service
public class IpfsServiceImpl implements IpfsService {

    // ipfs 的服务器地址和端口，与yaml文件中的配置对应
    @Value("${ipfs.host}")
    private String host ;
    @Value("${ipfs.port}")
    private int port ;
    private IPFS ipfs;

    @Autowired
    SubmitCacheMapper submitCacheMapper;

    @Autowired
    public void InitBeanDemo() {
        ipfs = new IPFS(host, port);
        System.out.println("初始化 IPFS "+ipfs);
//         ipfs = new IPFS("/ip4/127.0.0.1/tcp/5001");
//        System.out.println(ipfs);
    }

    @Override
    public String uploadToIpfs(String filePath) throws IOException, IOException {
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new File(filePath));
        MerkleNode addResult = ipfs.add(file).get(0);
        return addResult.hash.toString();
    }

    @Override
    public String uploadToIpfs(byte[] data) throws IOException {
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(data);
        MerkleNode addResult = ipfs.add(file).get(0);
        Multihash hash = addResult.hash;
        return hash.toString();
    }

    @Override
    public byte[] downFromIpfs(String hash) {
        byte[] data = null;
        try {
            Multihash Multihash = null;
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    @Override
    public void downFromIpfs(String hash, String destFile) {
        byte[] data = null;
        try {
            data = ipfs.cat(Multihash.fromBase58(hash));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (data != null && data.length > 0) {
            File file = new File(destFile);
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public String addIpfsById(String id) {
        //查询这个id所属的提交表
        SubmitCache submitCache = submitCacheMapper.selectById(id);
        //获取path路径
        String path = submitCache.getPath();
        //进行ipfs存贮
        System.out.println(path);
        return UUID.randomUUID().toString();
    }
}
