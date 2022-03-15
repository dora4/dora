package dora.util

import androidx.annotation.WorkerThread
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.methods.response.EthBlockNumber
import org.web3j.protocol.http.HttpService
import org.web3j.utils.Numeric
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object Web3Utils {

    const val RPC_URL = "https://api.mycryptoapi.com/eth"

    /**
     * 获取未来的某个时刻是否已经到来。
     *
     * calendarInFuture 未来某个时刻的calendar对象
     * candidateUrl 候选以太坊客户端的RPC地址，建议10个或以上
     * errorReturnTrue 当错误（小概率事件）发生的时候，是否提前解锁，即return true，如果不想数据永久丢失，
     * 应该设置为true
     */
    @WorkerThread
    fun isFutureTimeArrived(calendarInFuture: Calendar, candidateUrl: MutableList<String>,
                            errorReturnTrue: Boolean = true): Boolean {
        if (!NetUtils.checkNetwork()) {
            // 没网，不解锁
            return false
        }
        var timeoutCount = AtomicInteger(0)
        // 保证有个默认的RPC_URL
        candidateUrl.add(0, RPC_URL)
        for (url in candidateUrl) {
            try {
                val web3j = Web3j.build(HttpService(url))
                // 获取以太坊区块数量
                val blockNumber: EthBlockNumber = web3j.ethBlockNumber().send()
                val blockParam = DefaultBlockParameter.valueOf(blockNumber.blockNumber)
                // 获取基本信息，不需要交易数据
                val blockInfo = web3j.ethGetBlockByNumber(blockParam, false).send()
                // 获取以太坊节点区块的时间戳
                val timestamp = blockInfo.block.timestamp
                val blockTimestamp = timestamp.multiply(
                        Numeric.decodeQuantity("0x3e8")).toLong()
                android.util.Log.i("Web3Utils", "${RPC_URL},以太坊最新区块时间:"
                        + SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(blockTimestamp))
                if (blockTimestamp.compareTo(calendarInFuture.timeInMillis) > 1) {
                    return true
                }
            } catch (e: SocketTimeoutException) {
                timeoutCount.getAndAdd(1)
            } catch (e: Exception) {
                return errorReturnTrue
            }
        }
        if (timeoutCount.get() == candidateUrl.size) {
            // 全部节点不可用的小概率事件发生时
            return errorReturnTrue
        }
        return false
    }
}