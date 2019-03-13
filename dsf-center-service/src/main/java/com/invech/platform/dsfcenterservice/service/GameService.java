package com.invech.platform.dsfcenterservice.service;

import com.invech.platform.dsfcenterdao.mapper.DsfGmGameMapper;
import com.invech.platform.dsfcenterdata.AbstractGameService;
import com.invech.platform.dsfcenterdata.dto.LaunchGameDto;
import com.invech.platform.dsfcenterdata.dto.MemberBalanceDto;
import com.invech.platform.dsfcenterdata.entity.DsfGmGame;
import com.invech.platform.dsfcenterdata.entity.DsfMemberUser;
import com.invech.platform.dsfcenterdata.enums.Available;
import com.invech.platform.dsfcenterdata.enums.ErrorCode;
import com.invech.platform.dsfcenterdata.enums.GamePlatform;
import com.invech.platform.dsfcenterdata.enums.Terminal;
import com.invech.platform.dsfcenterdata.response.RRException;
import com.invech.platform.dsfcenterdata.response.RedisPageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.invech.platform.dsfcenterdata.enums.ErrorCode.DSF_BALANCE_FAILED;

/**
 * @ClassName: AbstractGameService
 * @Author: R.M.I
 * @CreateTime: 2019年02月23日 21:49:00
 * @Description: TODO
 * @Version 1.0.0
 */
@Slf4j
@Service
public class GameService extends BaseService {

    @Autowired
    UserService userService;

    @Autowired
    AdminManageGamesService adminManageGamesService;

    @Autowired
    DsfGmGameMapper dsfGmGameMapper;

    /**
     * 开始游戏
     *
     * @param gamePlatform
     * @param memberUser
     * @return
     */
    public LaunchGameDto launchGame(GamePlatform gamePlatform, String memberUser, String gameCode, Terminal terminal, String homeUrl) {
        if(terminal ==null){
            terminal = Terminal.Mobile;
        }
        DsfGmGame gmGame = new DsfGmGame();
        gmGame.setPlatformCode(gamePlatform.platformCode);
        gmGame.setGameCode(gameCode);
        try {
            gmGame=dsfGmGameMapper.selectOne(gmGame);
            if(gmGame == null){
                throw new RRException(ErrorCode.DSF_START_GAME_CODE_ERROR);
            }
        }catch (Exception e){
            log.error("开始游戏传入gameCode 错误");
            throw new RRException(ErrorCode.DSF_START_GAME_CODE_ERROR);
        }

        //第一步判断玩家是否存在第三方账号
        DsfMemberUser dsfMemberUser = userService.checkDsfMemberUserExists(memberUser, gamePlatform);
        AbstractGameService gameService=gameService(gamePlatform);
        String gameUrl = terminal==Terminal.Mobile? gameService.launchMobileGame(dsfMemberUser, gmGame,super.getSiteApi(gamePlatform,dsfMemberUser.getSiteCode()),homeUrl)
                :gameService.launchGame(dsfMemberUser, gmGame,super.getSiteApi(gamePlatform,dsfMemberUser.getSiteCode()),homeUrl);
        return new LaunchGameDto(gameUrl, dsfMemberUser);
    }

    /**
     * 获取玩家余额
     * @param gamePlatform
     * @param memberUser
     * @return
     */
    public MemberBalanceDto memberBalance(GamePlatform gamePlatform, String memberUser){
        //第一步判断玩家是否存在第三方账号
        DsfMemberUser dsfMemberUser = userService.checkDsfMemberUserExists(memberUser, gamePlatform);
        BigDecimal balance  ;
        try {
            balance = playerService(gamePlatform).playerBalance(dsfMemberUser,super.getSiteApi(gamePlatform,dsfMemberUser.getSiteCode()));
        }catch (Exception e){
            if(e.toString().toLowerCase().contains("read time out") || e.toString().contains("余额获取失败")){
                log.info("余额获取失败");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                balance = playerService(gamePlatform).playerBalance(dsfMemberUser,super.getSiteApi(gamePlatform,dsfMemberUser.getSiteCode()));
            }else {
                throw new RRException(DSF_BALANCE_FAILED) ;
            }
        }
        return new MemberBalanceDto(dsfMemberUser,balance,gamePlatform);
    }

    public List<MemberBalanceDto> memberBalanceAll( String memberUser){
        List<MemberBalanceDto> memberBalanceDtoList = new LinkedList<>();
        for(GamePlatform gamePlatform : GamePlatform.values()){
            memberBalanceDtoList.add(this.memberBalance(gamePlatform,memberUser));
        }
        return memberBalanceDtoList;
    }

    public RedisPageHelper<DsfGmGame> gameListPage(GamePlatform gamePlatform, String siteCode, Integer pageNo, Integer pageSize){
        // 走redis缓存，自己实现分页
        List<DsfGmGame> dsfGmGames=adminManageGamesService.gameList(gamePlatform,siteCode);
        dsfGmGames =dsfGmGames.parallelStream().filter(it ->
                Available.Available.code.equals(it.getAvailable().toString())).collect(Collectors.toList());
        return new RedisPageHelper<DsfGmGame>().getPage(dsfGmGames,pageNo,pageSize);
    }

}
