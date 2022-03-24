package dora.util;

import androidx.annotation.WorkerThread;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.SSLHandshakeException;

public class Web3Utils {

    public static final String RPC_URL = "https://eth-mainnet.token.im";

    /**
     * 获取未来的某个时刻是否已经到来。
     *
     * @param calendarInFuture 未来某个时刻的calendar对象
     * @param candidateUrl     候选以太坊客户端的RPC地址，建议10个或以上
     * @param errorReturnTrue  当错误（小概率事件）发生的时候，是否提前解锁，即return true，如果不想数据永久丢失，
     *                         * 应该设置为true
     * @return
     */
    @WorkerThread
    public static boolean isFutureTimeArrived(Calendar calendarInFuture, List<String> candidateUrl,
                                              boolean errorReturnTrue) {
        if (!NetUtils.checkNetwork()) {
            // 没网，不解锁
            return false;
        }
        AtomicInteger timeoutCount = new AtomicInteger(0);
        AtomicInteger confirmCount = new AtomicInteger(0);
        // 保证有个默认的RPC_URL
        candidateUrl.add(RPC_URL);
        for (String url : candidateUrl) {
            try {
                Web3j web3j = Web3j.build(new HttpService(url));
                // 获取以太坊区块数量
                EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
                DefaultBlockParameter blockParam = DefaultBlockParameter.valueOf(blockNumber.getBlockNumber());
                // 获取基本信息，不需要交易数据
                EthBlock blockInfo = web3j.ethGetBlockByNumber(blockParam, false).send();
                // 获取以太坊节点区块的时间戳
                BigInteger timestamp = blockInfo.getBlock().getTimestamp();
                long blockTimestamp = timestamp.multiply(
                        Numeric.decodeQuantity("0x3e8")).longValue();
                android.util.Log.i("Web3Utils", url + ",以太坊最新区块时间:"
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blockTimestamp));
                if (blockTimestamp < calendarInFuture.getTimeInMillis()) {
                    return false;
                } else {
                    confirmCount.getAndAdd(1);
                }
            } catch (SocketTimeoutException e) {
                timeoutCount.getAndAdd(1);
                continue;
            } catch (SSLHandshakeException e) {
                timeoutCount.getAndAdd(1);
                continue;
            } catch (UnknownHostException e) {
                timeoutCount.getAndAdd(1);
                continue;
            } catch (Exception e) {
                return errorReturnTrue;
            }
        }
        if (timeoutCount.get() == candidateUrl.size()) {
            // 全部节点不可用的小概率事件发生时
            return errorReturnTrue;
        }
        if (confirmCount.get() == candidateUrl.size() - timeoutCount.get()) {
            // 全部可用节点都确认
            return true;
        }
        return false;
    }

    @WorkerThread
    public static long getEthLatestBlockTimestamp(List<String> candidateUrl) {
        // 保证有个默认的RPC_URL
        candidateUrl.add(RPC_URL);
        for (String url : candidateUrl) {
            try {
                Web3j web3j = Web3j.build(new HttpService(url));
                // 获取以太坊区块数量
                EthBlockNumber blockNumber = web3j.ethBlockNumber().send();
                DefaultBlockParameter blockParam = DefaultBlockParameter.valueOf(blockNumber.getBlockNumber());
                // 获取基本信息，不需要交易数据
                EthBlock blockInfo = web3j.ethGetBlockByNumber(blockParam, false).send();
                // 获取以太坊节点区块的时间戳
                BigInteger timestamp = blockInfo.getBlock().getTimestamp();
                long blockTimestamp = timestamp.multiply(
                        Numeric.decodeQuantity("0x3e8")).longValue();
                android.util.Log.i("Web3Utils", url + ",以太坊最新区块时间:"
                        + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blockTimestamp));
                return blockTimestamp;
            } catch (SocketTimeoutException e) {
            } catch (SSLHandshakeException e) {
            } catch (UnknownHostException e) {
            } catch (Exception e) {
            }
        }
        return -1;
    }
}
