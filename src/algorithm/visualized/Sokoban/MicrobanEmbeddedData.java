package algorithm.visualized.Sokoban;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.InflaterInputStream;

/**
 * Microban 155 关与预存最优解答（内嵌，不依赖 classpath 资源文件）。
 * <p>
 * 关卡：David W. Skinner Microban；解答：martin-t/sokoban-solver 推箱最优解（完整 LURD 移动串）。
 * 重新生成：python scripts/embed_microban_java.py
 */
public final class MicrobanEmbeddedData {

    private MicrobanEmbeddedData() {
    }

    public static final int LEVEL_COUNT = 155;

    private static final String XSB_B64 =
            "eNp9W0mu4zoM3PsUBKiVPyDEcUb0Rvc/1bfEqSg7rxt4cWxroiiyWGT+0W1Z+Pi3MNX+h8b12o6L"
            + "/q1Q3Ox//tEm748WJG9xGx9lHR9VPuQZa6v7soxexi276u/oCP2eXNVjHs2bSut98a/aSkdaS8M7"
            + "LJ0v46s0fRwDW1NiffG4qKWOFlSarlHvpM6kj6cNT7jwPpSsu0iTMZWjs1qrPiGKaWtfL1/KwrEQ"
            + "HbrUcvmdGszo6ORt0uyL6oupTS5KGauXmXYxMPmHLMAmLzK3WZLui7aWUT461YWrtGije3vPRM1c"
            + "ZYzR6LssKkGd3biutiv9mmMtcn0sr4z/MqvxP3Z60oXtNi3eZn3ca6pfrH3ValehNAQb0rvbfJUm"
            + "l0KqGaRac3RcbRCqTD5uiEv6uttZqqOnOvS4UundFVIVLKqBpolkg4wu9iv9YNmho6e1uV65Sm0P"
            + "lbnfFEH0W34YOQRZ2I4I4WEZXT31uNjE4tAMkZjO90NKrZjURO9EOHnDRqevELFIpHZTU0pBI5Lt"
            + "xfa+kMMxqsqDZadkc4ds+0Y02xnblt7RZ7IdVU+5KbMcazkkdP2pPX1PU2LraSxIepq1w8Q4zese"
            + "dtesYV+e2JNhSlxD3IZOqooSsTOso+hR/aVl93tayuo7YH2Md0ur2oXNojftCkp4+mQHCipmGDZZ"
            + "UizhMenX4sozzKbKk85KfjfNHE0a+cx0+/thr2qNzcmAtF/Ja9WsRDJ3V+087jsJ3VS4b9J4S1ZI"
            + "5836xKE0O6gWxq+jgUlejaVYoTLMPpu4uYYhm4/Y/Zu0ycVgMgWR+sp2N6Rmyw+7Uaq1zvs3uYZ9"
            + "s31knvYcnLI/d8XbUfGqKOs4xmYoW/rO2e3t+08UMV5sZV3XSr88x/5IkzbLLD42/SWzTA3dnfby"
            + "zAcXjEvx/800ckCBecO8qxc4SzNF1WWnV0XvFZOLGPifNnwP04nyUcNZDQWMTa66y92UB4BKPY4u"
            + "P5PlMadkKllsBYfVCIvUwn2lI2S9fvFI+wlQBdRD/V82II/bhV9o423ZEbECBnJiCY9tcVuOtt2g"
            + "ZjFpVxVOBajkndzhUNs5bnGi7VXyrdOrw1pQdMhnA/XYXcKt1JDR4xGGyw2kAqXCCNEbus+HW8tw"
            + "JCy4cAl4dBh+EdbAC7Olf7wWQDfMYdW7hps+BSZuw1oA8AVM7n2+0Q43nRVp+KF7X33zowsCXzw6"
            + "+kBHGqwUpoBvVMg00t2NLG+cyRQMhNi+ZwNjBqlVO4WBlxVSm79HODjihttslcNBHkhOO0FAGR30"
            + "5luaTcPdXit+XLm8591P1/BSY9iVVpO57+TQgBanpbfdE4p36J6mqNBD5VStw6H0spEDhKgHAVcY"
            + "C3wkbNbcH4DBLGHnzFMd92rVS7JAx5f9PIEkt5tr+OqW5vEKT+gwDcCka9GY2uo+y+ZYc8j5fM/Q"
            + "rq7mTwubvB3BoNw/yzL5FMrmWyTfPH6DZzolj0k9GpYPUoREqrVh5tHSP78RLwf4V3DAEUI64pTL"
            + "YtxBEbfVP4nCg4VbwyhFAuLbdXDvY/UD2/ROYVi5WlbT/kBXw8lFSPoK0w+xnyzbeyndVyOb4M+I"
            + "Y/9tzgZjCEBIsvyky07mU14YFiS0y0MEOFv4d4y3X5pzMwYOeavj8h5ZNzWZVV5UaBmiGT0/fkXT"
            + "0saOILut6HIy5Ezmdig5D+36aWJflB2JHeyhIpnLNp0Nnbq68vd0JqM7sWuBgmac+nqdons2qFh9"
            + "IhKFsJEauYf3HBH7HtiZZneHdSxPnFjyxy892KEuHAoW5ljkr369r02GaAScxpjwDCj7EN8UZU+n"
            + "qZiSlKzXA6WSxl+2gznufd+SUTLY5dZ1EGh9OyOq4kAa5jliM/2b/aNk52ak3GewTZGK70chDwUL"
            + "gAWfaFBcwAgZVeE7YVzQ+z5BezGkJTslCxz7OWgBglDa0wb1rvezGo0Y+7DlGpB2x1bIEeM4xG41"
            + "EYSher6R32QJOdAemZ0oEsow6TE2gDYj/fcTUAMwMy4Ww2RtXBo97NgtcPDwBsWO1IkM0eFeU6zS"
            + "xgpAQQsbraETVwgG/hNQRe/ynSXidKfA1ojTdC8dIDaSgA2mzJnfe38unKMbpc6rOZPc6A9C7P09"
            + "hZBy4M1/FOHuXNuoptHcg4tMW7GZOv97wpKf2xxrBTJTQLMqKGcjP08RwedMQAniH+CK0R6OvcRz"
            + "gUHc5z7lEByKytDGkPpElaJTozgIILsIjnb0PGUKXJfijAa/ezgBH8iv3HokeEUY0lCOaT5Gv+Jc"
            + "zIrO2lpwJNdRdhrPneEIp+wpW/Oa45WPuFg4VIYnHZ6O8XQ7nNWpvnsVYc3nheFtrCLR512kBvGc"
            + "9zA8YmeuWIagzXr4ThE0UwgquEomWzEL+eH38G6QtJ7esPtj3EAO5KEviO5zYkkioXMsoEXKJNCT"
            + "iVdsn175rGsKUscgiKbHDjYKtSgeWnicUqvL0pJIaDi+V664smP+I0YtwfTVC5Tw3aZQBylK/57N"
            + "NUfKRmKMIJ07rxFpHPfueeARS3moAeQLnppjavdkXZmDMWWIPgWZhs6tdfXhanOvHven970f6J+S"
            + "iMCGeFiaeY+h+xQm5pxjiSVarynWbZD681yfJvv8wh+dM5Lf50/YnhKhbNGQAYGGYSuwKoRZkBHO"
            + "AcYD3uX7OtFoKagqVNSJauDnMKQx6LlTNClpMXPo38y9RxA+HB45H1PMVxbX1jBMJGERbL8QSw0j"
            + "40zfhP9fKAXIY1KfBXT3lOpEJY898w4o0C7HgaPI8jL7TQSxCuwomWTzQwJSC+geZUCdTh4AmPkE"
            + "pgxXU5K8kLPxA4zVwnDnB97dbpGmTfEZB/glTT255NUCYwZtdf4F08IH1viPIGh0nOHJLJA2bgfu"
            + "V5/kNtlTCW4RxRfIwIMztKjYjOKU1UmYervdsyyqUZOeZdHEBxsj74Q6ePzR074A1w+pI4hHBxJt"
            + "JjWTuyAhwAhoh717iyZSbodCuj7aWntq2dJv6vmFWaP5dno9OYgEWWzUPo/n7DfNNDSL5IQOijQL"
            + "ZmBiZxg3BvdljPK6Zp56FomELaX5AvNLbdL49+LEEgRMl36MnUxhO8VhAwSj+vM4pAtjSUnSi0/i"
            + "6mkqHbBAASAHA09vppKrI8bqKNKi+sIRj1TULsTC2+17QaWKXV6YI19dwnYMDsfx2wI5FS/RuM3x"
            + "gmMf8zR6Ba6XcXzhiyRWintmdkpgyRnBbds2BcTmggxMAw9HRS2RkJ84jxQ2LIaTNJTRoDMHGzOd"
            + "tm33oDGXQKvqwlQKkeeJqFLyqGrRmjllp64NrRvTGV/FtJbkB2n+tkxOZIsSlpTw8P0uxhkmoqY6"
            + "bCgYn5/j+83KXSYbmKkpgl7D1pCJ/4DKBSokCOO2McZzzt5jPsun2jspBTx6MsrJ2PQiGDBylGwf"
            + "AZVaazLqRjD0EUtQZ8BLFUwgJ8A2xn3P+uvskiFaaVrULylCsgBOMztDc/SFH+r5uYCk1ocFPcVC"
            + "uFYiqIXqNIgxocpmwWFSgRngqyVCbaFQACo1Dp5GiE5z8LUCVPSGi+dyR1XXLdh3D9V7wpiwesZC"
            + "6QJsHTG4zAT/Gqj4nIi0YS/YFi6xUsjfUYmV2sDZr4WhnIIJSQmlAwzVQRcccipo7OOpTSdxa6SU"
            + "UBQnVNOr/lxFEo8BofIZnUSi3Se3L1MlBZwTm6glHmsFQ6O1WZmUpsxKU6alZxg9E9Pb/XFZ7oix"
            + "AyMgsVy7aFEwkpYxYjvqnPPnYEjuiI00u1vDzlGRfw3AFpMXbNBcmRpVhFIjRZxMiXIVpXppGFVg"
            + "GCKeq5Rj5bnM8g41q0Bl69srFS9/E/Brx351ElDDoyXC0pj659x5S52vFTp3wa4MXYYtnjr/erIv"
            + "18BUK9crEMONo1itaqoZ82Qc1FQkoDWft7nGbLQXNmORQD3HzWqCSi7uw0qrbd+cZG6ahPYFdF8C"
            + "LhaIN+ZMrIMc9vtUSq3ATU6aF4pFRqcVzFDmNG0iFbZ9XzKMiEPouuY2TwGMQ89Ri2qOq0oObqE4"
            + "2IF9Z1oLy0T3x0V1J3s5pqZiEhNaIiWpULkyJppHt8+r+nO2ZLaG11YupjXwlaPQIwlk9Pia6Gby"
            + "THYc3AgGjZVxVjGciIVr5jI0GDHCnTImxUW9pzIZBHpSwW+pHKgVgxhIa+bduhWPoM1//hVO75+p"
            + "+NCWVqeLoAXanMQ3FJx8j/F7DvfIowQ146i8uTwGphcsypyJnICduQbJ1bNhscoxnn7zLwCaT3TZ"
            + "9rihGbTYozqfUtfqd1ejzIcVtykVL51yjWl8UdH92K7CPJdvcRq7NmNHj3ugoFf11Y/7qSQ6EutR"
            + "4mXec/HqK9VVCK48xWMNigW//QRUKGplzKZujx3oTU655lIiHzWc1ShfJzm2NSLrdXV6vNK6GmdV"
            + "+hp04FK6WCLDpmk7TgAe5PJIgZ+VPxNHQW8vmauRRHDxHzfh2u7D+9ELkiQhj+eUTK1Qo2e8NXnt"
            + "bNNCIL8X73lbDLMe2ZYBSrJS7N6ocQUqBdNakEyBjWKPXaAoJG/z+5RHqQFG+rgNQkhOEkC6lXjm"
            + "WU8I8fE5VUoHB+86AA6iBB3AUfPAqUjlx29HHt+cXIMspNaVYGAyuKWIuQsXWHYugnJfnF2O/mTk"
            + "dlFZ6y3DkWno6jEEFIR4yUOztDcbxMolUWO8Lf0cawr6JRuGVS4QRBfyjCNhfQpHhYovVBSwiVVc"
            + "8XcMuL3P+xSOiDUPSrxKUYfa0ypEiD8TZVVe3Z/lR4tU5pgtlC+pND5msy9TkDJPOUU0c8YihT/T"
            + "vxw6nXIdDh+vWvrTy5bx9KolPL1oiU/PLdPTv9vSX+PyX3Omv9Z7veJFfoP1Q1qidr8kvfCf+9uV"
            + "APkxvkhVSRfr9PsF9z5Qvn09gSknBtWKSAdjvin+Ln1cqXmOeBTzZStHVkzDEcqdG2kNzwiZtO6D"
            + "MyWABtMNAMwrISlKzxzDRevu4vliWwziriDZlDRCKWP6LtfyOmegl2v+Ian6wWbXK/xqA2tVA5L+"
            + "D9Q2oF0=";

    public static final String XSB = decodeXsb();

    /** 预存 LURD（小写），索引与关卡一致；null 表示暂无公开最优解。 */
    public static final String[] SOLUTIONS = new String[] {
        "dlurrrdlullddruluruuldrddrruldluu",
        "rddlruulduullddr",
        "ruullluldrrrrddlurulllddllluurrdrdluuurdd",
        "ulldlurrrdllulllddrrrurrddlurul",
        "luurrdlrrddlulldruuullddrddllur",
        "ulldllllulllddrluurdrrrrrdrrruulldlllllllddrrudlluuurrrdrrrrurrddlllulllulldrddlururrrrdrrruulldldlurrurrddllullllullldrurrdrrrrdlulllulldrrrrrdrrruulldurrddll",
        "rruulurdddlllluruurrdlrrddluruuuulld",
        "llddddddldddrruuluuuuuuurrdlulddddddlllddrruddrruulrddllulluurrllddrrdrruuludrddlluuruuuuuulurdddddddldlluurrdruuuuuu",
        "urrdulldrdrluurdrddluruldlurul",
        "rddrruullrrddrruullrruuurrdddllddlluurlddrruuuuddddlllluurrddlllluurlddrrrruurlddlllluurrrrddrruuudddlluurlddrruu",
        "ulllddddrrrruldllluuuurrrddldurrddlluurdldllullddrulurdrrrurrdllllullddrurrruuruullldd",
        "uululldrdrluurdrddrddlluruuulldrurddrrrddlludllur",
        "dddruluurdlddrdrruldldluuuurulddddrruldluuurdlddrruldluruulludrrulrdddlurul",
        "uullllddrdrulluurrrrddlllrrruulldurrddlldllurruulld",
        "drdddlluuddrruuulllldllurrrrdddrruulrddlluu",
        "lddrudrrulldluuurulllulddurrrrdrdullddrdrrullruulullllddrddluruluurrrrdrddluruldlurulrddddluruuulllulddduurrrrdddluruullllddrddllurdruluuurrrrddlurulllulddddldr",
        "dldurrdulldrddluurddrruluddluu",
        "ruuuulllddrrdrudddluruuudllluuurrrrdlddddluruuuulldlddrrdrullluuurrrdlulldddrrrudllluuurrdlurrrdl",
        "urrdddlddruuuurullldrurdddlddruuuullllurr",
        "urdddrrruulllddlddruurrruullllrrrrddllludrrruullulldrrulllldrrurrdrlulldrr",
        "dlludluluurdrddlu",
        "drrruuldldlurrrdlulluulurdddrrdluluuurdlddrddluuuuruldddrruruul",
        "dlurruullurdrddlludrruululldrddrruurullrdddlluururdlllurdrrd",
        "ullddlddrruluuurrdluldddullddrruuld",
        "urrdlullddrulurluurdlddrrruulluld",
        "rdddlurdddluulldrurrddlurulruuulldrurdldurd",
        "dlulddlluuuurrrdulllddddrruulrdrruluullldddurrdruu",
        "rdulldrddrruldluuluurrddldruuuldd",
        "ullldrurrdrrddllllllluururrdrrulllldrurrrdrlullldllddrrrrrrruullullldrrllllddrrrrrrruuuuddddllllllluurrrrrrllllllddrrrrrrruuuruulduulldrdrduluurdrddlduruuld",
        "drululuurdlddrrruulluld",
        "rldldrrdrruulluld",
        "ulldullddrururrdlllddruluruuldrrrdl",
        "ulllrrrddllrruullldlurrdllddruluurrrrddlllruullddrrul",
        "drrrurdlllurrluurdldllurdrdrrrruullluld",
        "lluluurdrdluuluurdrddludddluruuruldddddrruldluuuuludrruluurdlddddluruuurdlddrdlddrruldluruluuurdddlddrruldluludrrul",
        "dllllllllllluurdldrrrrrrrrllllullrrdrrullrrdrruldllllllluurdrdrrrrruldllllulldrrrrullrrdrllllluurdrrrdrrulldllulldrrrrulldlluurdldrurrrrrrrdrrllullllllldrurrrrrrdrrurrdlullllllllldlluurdrrrdlurrrdlurrrrrrdlulldlurrrdlulllldlurrrdlurrrdlulldlurrrdll",
        "drruullulldrulullddrluurrdrdrullullddrrurrdrlulluldrdrrddrruuddlluuulllldrurrrdddrruuudddlluuullldrurrdddrruuuuddddlluurlulldrrddrruuudddlluurlddrruu",
        "llluuurrrdduurrrrddlllrrrddlllullrurr",
        "ruullluullddrluurrddrrrddlurullluullddrrddllddrruuuurrurdllllluurrdullddrrrrdruddrrul",
        "udrruluullddrluurrdduuldd",
        "urdurrdulldrurrdddllluluurrdrullldrurrrdullldddrruruuldulldrrurd",
        "rdddldllluurrruruuldddlllddrrruruulduuurdlddlllddrrudlluurrdrlur",
        "dddllldldllururrrrdruudlllllddrdruudlluurrlldrurrrurdllllddrulurrruuurd",
        "r",
        "ruldlluuuurrrdlullddddrrulruudddlluuuddrruuruldddlluurlddrruu",
        "dlluururdrudlllddrrudlluururrdlddlluuluururrrdd",
        "rrruuulldrrddlllluurrrurdduurrdluldllllddrrrrrrdrrulllllldlluuurrrrdulllldddrrurrlldllurr",
        "rdddurdllrrrddlludrruulluuullddlddruuluurrrdddlrddruluuuulllddrduluurdurrddddrrulll",
        "ldddrluuurrrddldddldrrluuullurluurddldruuurrddldlluurdrdddlddrrulrdrruldlluuuuulldrurddd",
        "llulddulldrrurrrrrdddllllluulurrldddrrrrruuulldllrrurrdddllllluururrdllllurrdrrurldllurr",
        "rddlluluruldlluurdldrrurdlddrrruul",
        "ddlruuldldlddrrlluurrllddrrdruludllur",
        "ruruululldlddrdrruuddlluuddrruuudddllurdrulluuddrrruullrrddlu",
        "drururrlluldrrrrrllllddllulurrrrrrdrulllllldrurrrrrudlllllddrrudlluurrrrdrruuldlllddlururrrurruuldrdldlllldllurrrr",
        "rdrrrddlurdddluruuulllrrrdddluruulllulldrrrrurdddlurullrdddruuulllulldrrrrurddlddrrlluurdldr",
        "uullulldrurdrddlluruuld",
        "dddrdrruldllrruldlulldrruuudddruluuruulddddldrurrrddlurullldllurdrrurdlllurr",
        "luluurdrdulldrddrruruulllrrrddlluddlurrruull",
        "dlllllldddlurururrrruullllllllddddrrdrullluuuurrrrrrrrddlllllldurrdrullldrurrrrruullllllllddddrrdruulurrrrrrurdddrddlluruuulllldlullddrururrrdllurrruullllllllddddrruurddllluuuurrrrrrrrrddddddruuluuuulllllllllddddrruurrlddllluuuurrrrrrrrddldlllurrrrurddduulllllllddrururrrrrddrdluuulldlllllurrrdrrurrrdullldllurdllldllluuuurrrrrrrrrddrdduuluulllllllllddddrruurrrrrrurddd",
        "urrrrrrdddllulldrrrllddrddluuuddlldlluruuluuruurrrrrrdddduuuullllllddlddrddrluuluuruurrrrrrddddduuuuullllllddlddrddrrlluuluuruurrrrrrdddllulldrrrllddrddlullluuluuruurrrrrrddduuullllllddlddrddrrrudllluuluuruurrrrrrdddduuuullllllddlddrddrrruuddllluuluurddduuuuurrrrrrdddllulldrrrlldddlldllurrrlluuuuuurrrrrrdddlllddrddluuuddllluuuuuurrrrrrdddllulldrrr",
        "lddlddrruldluruuuruullllldddddrrrdruuuddlllluuuuurrrrrddllrdddllurdruuddrruldludlllluuuuurrrrrddlruullllldddddrrurru",
        "llllulldrdlllrruuldlddlluurrrurrdrullldrurrdrlulldrrldllulllddrrudlluurrlldrurrrlllddrulurdrrurdlllurr",
        "llllllulllllllulldrurdrrrrrrrddlurulllllllulldrrrddlururrrrlllllrrrrrrurdlllllllulldrrrrrrrrdrrrrrrllllludrrrrrdrrullllll",
        "ldlluululuurrrddllrruuldrdldddrrurrdllldluuuuruuldduulldrurrddlddulurddduuuruuldulldldruurrdddduuuulldrurddduulldrurd",
        "uuuulrddddlluururddullddrdrrulllullurrrurddullddrdrruluulldllddruruurrddlruuuuulddrdddlluururddullddrdrrullruulldllddruluurrrrddlluulldrlddrurrruuuuullldrurddrdddlllluurrrurddullllddrrrdrrulllldlu",
        "ddlldlddddrdrluluuuururrddrddlddlddrruluuuruuluurrdrddddldlruruuuulullddrddluuuddddddrddllurdru",
        "uurrddrdluuullddrrdrrddlluluruuullddr",
        "rudlluuurrrrdrrulllllulddlddrrruuddllluururrrrdlullldlddrrruuddllluururrlldduurrdrrullrrdrrulldllululddurrdrrullluld",
        "urrrurrrdrrddlluurullruurddllluldrrrdddrruulrddlluurrdluullldllrruuldrrrrdddluurullldddllluuddrrruulrddllluuudddrrruullrruuldrdddllluluurddurrlldldruuuluurdlddrddrluuluurdldr",
        "rdrrrddlurullldrulullddrddldruuuluurddddduuuurrdluldduurrrrdlulldldduuurrrdlulldddrdulldruuuurrdlulddd",
        "urdduulllddddddrrrrrrrrrruuuuulllddllldllurrrrdrulllluuullldrurrdddrrrrudlllluuulldrurddullldddddrrrrrrrrrruuuuullulldrrrllddllldllurrrrdruu",
        "lllddduullurdrddrrddlulldlluurdrruuulurddddrruldluuuluurddddrdllddllurluurdrruuuurrdrrullllddddlldurduruuullururddddrdlldlluurdldrlddru",
        "uulllluurddrruldlduluurdldrurrdrrullrdddddlllluuruurrurdddduuulllddlddrrrdrrulllrruuuuullrrdddddllldluudrrrruuuulllluurddd",
        "luldddrrlluurrdrrrurrddluruldllllullddrddrruuddlluururrrrurdllllldlddrruulllldrurrrddlludrruulurlullddrrurrurldrurldrlllldlddrruulurrrurd",
        "ddllululldrurrddrruulllldluulurddrrrddluruldlluuurldddrrurrrdlulldlluuurrlldddrruldluudrrrrdldlulululurddrdrruldlluuurldddrruldluulldrrdrruldluluurlddru",
        "rddrddrrrruuldrdllldlurrruuuuurrdluldddrddlllludrrrruuudddlllluluuuuurrddddlddrrrruuuluurrdlddldrdllldluuluuudddrdrrruuuruuldddrddlllluluuuluurrrddddlddrrrruuldrdllldluululuurulurddlddruluuururrdddddlruuuuullddlddruluururrdddddldluluuuurrurddduulllddrudlddruluuurrldlddruluururrdlllddrulururrdduulldrurd",
        "ddddrruldluuuddrrrrrulrdrruldlllllluururrrrrddrdlllllrrurdlllurdldlluuururrrrrddldllurdlldlluuururrrrrddrdllruuullllldldddrruurrrlldldlluuururrrrrdduullllldllurrdldddrruldluuuddrrurrrdllldlluuururrrllldllurrrrlldldddrruurrrrrdlllulldlrurrdlldlluuulurrdldddrruldluuururldllurr",
        "uurruurrddduuullddrluurrddllllddrrrdrrulldlluluurrrrdullllddrdrruruuuullldrdrrdddllluluuruurrrddddrrldlllluluurrrluurrdddullllddrdrruruuuuldrdddrluuulldrurdlllllurdrrrrdullllurrrdlllddrdrrurdlllurdrruuuluurddd",
        "llldllurrrrrlllluurrrdulllddrrrdrrulrruuldrddllulrdrruldlluuulldduurrddlrdrrul",
        "lddrudrruluuuudllluuluurdrrdrrrurddulllddllluuddrrrddddlluurlddrruuuuudllluuluurdrrdrrrdrulllddddlldrdruuuuudllluuluurdrrdrddllluulurdddrrruurrdddru",
        "luuurrdldldddrruluuuruullddrddllurdrddluruulldrdru",
        "drrruuludrrulullulldrddlddrrrruuuulluldrrrddlurullrrddddlllu",
        "urruuuululldddddrrruulurdddllluuurrdrdddlldlururruululluurrdrddddrdluuuuulullddrruuldrddrddlldluuuudddrrrdluruuluurddddrdlullluuurrurddduullldddrrrlllddruluuuurrrddddluruuuulullldrrurdldrurddddldruuuulllddddrlur",
        "urlddrluurdrdduuruluullllddddddrrrruuullurdrdddlllluuuuuurrrrddrddlddrrdrrullllllldluuuuuluurrrrrddddduuuuulllllddrddddrrrruuulldrurduuuulllllddrddddrrrrrrdrrulllllldlluuuuuluurrrrrddddduuuuulllllddrdddddrrurrrllldlluuuuuuddddddrrurrrrllllldluuuuuluurrrlllddrudddddrrrrrrdrrulllllldlluuuuuulurdddddddrruldluuuuuluurrllddrulur",
        "luuuurrurrdrullldllurdrrurrdlurrrdlulldllluldrrrurrrdlulldllurrrrdrruuldldlulldlldddldrrluuuuurrrrdlulldlddddrrlluuuuurrrdllulddddldrrlddrrrruuuullddlluuuuurrrrrrrdllllulldlddddrrrllluuuuurrrdllulddddldrddrrrruuddlllluurlddrrrruuurulddddlllluurrdullddrr",
        "drrrurruulldldururrddlldlldlluururrdrrruulldllldduurrrurrddlldllrruullurdrdrruululddllldlddrluurduurrduurrrddlldllrruuldrdl",
        "lurluurdllluruulurrrrdllldddrruldluuulurrrdrrruuldrdlllulldddrdddruluuluuurrrdrruuldrdllullldddrddrdrrulldluluurrdldluuudrdddrruldluluuuulurrrrllldddrrdluluuurrrdrruuldrdlulllldddrddluuuulurrrrrurd",
        "rrruulduruuldrdddluruuuuuldrdddddlldllurrruruuldduuruuuurrdddddddldluuuddrruuuuuuullddldduuuurrrdddddddllllruruudddlldllurrruruuluuuurrrdddddddldlurruuuuuuullddldddduuuuuurrrdddddddlllruluurudldddlruuuuruddldddldllurrruruluuuuurrrdddddddldluluuuuurulurrurddduulldddlddrudlddruluuuuuurrrddduuullddlddrudlddruluuruluurrrdddduuuullldddddruluuuurrrddddduuuuulllddrudlddruluuurrurdddduuulldlddruulurrurdd",
        "ddddruluuuurrddlruullddddrddlluluuuurlddddrdrruuluurruullddullddddrdrruldlluuuuurrddduuulllulldrrrrldddddrrruuldldluudrruuurruullddddruddlldlurrrdlluruuuuurrddlruullddddrdll",
        "ldurrrrdlddldlllurdrruruulrddldllurdrruuuullldduurrdluldrrrdddllllurdrrruuulllddldruuurrrddrrdll",
        "rrdrrruuldlrrdlullrrdlullullllddrrurldlluurrdrrllulldrrrlur",
        "uuullllllrrddllulldllurrruuluurdddrrrrrrruurrddlruullddllllddllulrdrruurrrrdullllddllullrurddrruurrrrdduullllddllulldllurrrurrrddlludrruurrrruurrddlrddlluuddrddluurruullllllddllulurrrrrr",
        null,
        "rdrrurddduulllulldrrldurrrurrdlddllldlluruurrrurrdlddrddluruluulllulldrrrrurdddlrrddlurul",
        "ddrduuurrdrrddlruulllulldddddrrudrruuudddllur",
        "llrrrrdrrullrdddlruuullrrdddlldlluruuullulldrrrrrluuruldddrrdrrullrdddllrruuullrrdddlldlluruuudddrrlluuuuuruuldduulldrurrddldduuruuldd",
        "urrrrllllddrrlluurrrrdddddllluuluuurrruurdrrrrrrllllllldllldddrddrruruuuulurrrdlulldllldddrddrruruuuururrrrllldlllllldddrrurldlluuurrrurdlllldddrddrruruulldlluuurrrurrrdllllllddrluurrrurrrrrlldllllllddrdddrruruuuulurrdlllllddrrlluurrrurrrdllllllddrrrllluurrrurrrrldllllllddrdddrruruuuulurrr",
        "dlllluuuulllllululldrdrrrrrrurddduulllllldurrrrrrddduuullllllddrddluuuddlluruurrrrrrurdddlddrluuruulllllldlddruluurrrrrrrddlddrrrrlllluurduuulllllllddrulurrrrrrurdddlddrrrllluurdldrrluuuulllllllluurrdrdrrrrrddddrdrrudlluluuuulllllulullddrrrrrrrurddullllllllurdrrrrrrrdduullllllluurdrdrrrrrddlddrrrllluurdldruuuulllllulldrrrrrrurdddlddruuuddddrruldluluuruuddlddrudrrdrrullrrruuldrdldlluurlddlluuuuddddrrulrdrruldlllurdrruldlluuudddrrurrruuldldllrurrrdldlllluudrrurrdllldlu",
        "rrdrrrrrddrrrrrrrrrruuldrdllllllllldluuuruuldrdlllrrddrrrrudlllluulllllulllddrrurulrdrrrrrddrrrrrrurrdrruuldrdllllllllldluuulllllullrrdrrrrrruuldrdllllrrrddrrruruddlllluullllrrrrddrrrurrluulurrdrruruullddrdlluldddrdlllldluuullllluullllddrdrrurrrrrrruuldrdllllrrrddrrrurrrrrdrruuldldlllllllluullllrrrrddrrrurrrrrrrdllllllllldluuulllllululldurrdlulldddrrrurrrrrrruuldrdlllllluulldrullldddrrrudllluuurrrrdulllldddrrrurrrrllluulldrdrrrrrrlllllluurdldrrrrllluulllldrrrdrrrrrllllluurdldrrlldlludrrurrrlluulllldrurrrddrrrllluullldrrurdldrrr",
        "lddrduluurdrrrdldddrruulrddlluurrdluuullllddrudlddruuluurrrurdddlurullluldrrrrddrddlluuruulllldurrrrddrdluuullllddrulurrrurdddlurulrddrddlludrruuluullluldrrrrddrddlluuruulllldurrrrddlurdrdluuullluldrrrrddddluuurulll",
        "ruullllddllddrrurruudrruuuulluullddrddrddrrrruuddlllluurlddrrrruurruulldllddullddddrrddrruuluul",
        "rurrurrrddddlruulduruuldlrrddluruulldlurrrdddddluuruuulldlldllluurrdrrurrrdlulldldllrurrurrdlllddlllurrrurrrrddluruulldlldlllluurrrdrdllrurrurrrdllllddlllluuurrrdrddllulrdrruurrrddddllurdruuurullllddllululurddrdrruludldludluudru",
        "dllddllurlluurrdullddrdrrudlluluurrddurluuurrdluldrdlddruuu",
        "ulllldllurrrrurrddddlllluuddrrurruuullddduulldllurrdddrrlluuurldddrrurruuulldduurrdddlduruuulldddrruulrdddllurruuulldrrdddllddruluurruulldd",
        "rddrluuuulllulldrrrdrrdddrdrrullluuuurlddrduluulddllurdddrlluruururrdlddldlldllurruruuldduurrurrurrdlldlddruu",
        "urrrrrdlllulldddrrrruruulldlluldurrrrrdllrddldrddrruulrddlluuullldrrulllldrrurrruruulllldlduurrrdllurrrrdlddduuullrrddllldrurrdrrddlluuulldrllllurdrrurrurululllldurrrrdddldlllurdrruruuulllldduurrdlurrrdlrddldllurdrrlllllurrdrrruuullulldduurrrrddddlllllurdrrrrddrruulrddlluulllluuuurrdluldd",
        "llluruuldrddllluuurluurrrddduuulllddrrdrullluurd",
        "rrrrdrrrrruruululldurrdrddldlllurdrruruulullddduuurrdrddldlllllllulllddrrurulrrrdrrrrrulrruululldddrdllllullllrrrrdlullddlluurrrrdlulllddrrudlluuudddrrurrrrruruuurrdduullddddlllulllrrrdrruruuurrdrddllruuulldddrdlllllulllrddlluurrrrdlulllluurlddrudddrrurrrrrurrruulrdddlllllllldlluurrrdrrrrurrruuulldddrdllllllulllddrrurullrddlluluurdrrrdrrulllllulduuurdlddrrruulluld",
        "ldddrdrdrrdddlluulurdddrruuuuuuuuuullddlldddrdrdrldddrruuuudllulululldrdrdrrlluluurdrdrddddrruuuuuuddddddlluuuululuuuuurrrrddlluulldddddrdrddddrruuuuuuuuddddddddlluuurlululldrdrrlluurduluuuuurrddrrdddddduuuuuulluulldddddrdrdrlululuuuuurrddrrddddduuuuulluurrddduuulllldddddddruuluuuuurrrrdddduuuulllldddddrddrrlululuuuuurrrrddddduuuuullllddrrrluurrdddd",
        "uullllurdrrrddlurullrruurrdluldulluurdldrrdrruldldlludrddrulululldr",
        "duluurdrllddrruruuurulllrrddddldlluurrdruuurulddddrdddlludrruuuluuuuulldlurrrdlrddddrdddlluuudlluurrdruuuuruldddddldlururuuuulrdddddldddluruulldrulurdddruluurlddrddrruuuluuuddllddruruuuruulldrddddllurdruuuulurdddddrdlullddrulururuuuullrrddddlddruuuuulululldrdrrllulldrrurldr",
        "rrruuuuruullldllluururrdulldrllddrrurrrlldlddruluurrrurdddrdluldddduuuuruulllldlluururrdulldlddrrurrrurdddlddddrdlllurruuuurrdluuuulldlldlluururrdrrrdddlddddrdluuuuuruuulllulldrdrrurrddddlddddluruuuuruuulldlluurdrdrurdddrdlulddduuuruuulllldrrurrddddldddrdluuuuuruuulldllddruluurrdrurdduulllldrurrrdddrdluuuullldrurrdddldddduuuuruuulldrurdddlddddrddluruluuuurrdlulddddrddluuuuddllrruuudddllulldrrrlllddrrurrlldllururlddrurrdru",
        "rrrdrdrluluurdlddrrdrullluurrdullddrdrrurulddlluuddrruullrullllrrrdddruluullldlurrrrddlurulllulllrrrdrrrddrrululllluluullldlddrurrrrddlurrrddrulullluudrdrrdrrulllldluulllluurrururrddlruulldldllddrrrrrdrrrdddddruluurrdldluuruldlurullluldulllluurdldruurururrddlduruulldldr",
        "dddldlluuddrruuruurulddddldlluuuurrllluurrrdrdululllddrddddrruurrrdlurrrdlullllddlluuuuluurrrdrdduululllddrrrurdululldldrddddrruldlurrurldlludrrurrlldlluuddrrurrdlurrrlldlldlluuuuddddrruurrrrdllullddlluuuuluurrrdrdduululllddrddddrruurrrlllddlluuuurrurddulllddddrruurldldlurrurrdlldlluuudddrruuruuululllddrrrurdduululldldrrrllddddrruurdldlluuuurrurddullldddrdru",
        "dlluuudddrrrrudlllluuulurrrdrrruulldurrddllulllddddrrrruuddlllluuuurrrurrdrdddlruuululldlllddddrrrrurruuulullddrrllulllddddrrrruuddllurdllluruluurrrdrluurrdrdululldlllddrddrrrudllluuluurrrdrrllulllddrddrruruddlllurrlluluurrrurrddlrurrdlululldlllddrdrrdruudrru",
        "drrrdrruuruulldrllldurrrdddlluurlddrruullllrurdlldllurrdrurrrddlluu",
        "lllllddrrrdrddludlluruddrruulullldllddrrrllluurrurrrdrddlulldllluurruuurrrrrrddddllrruuuulllllldddllddrrrurudldllluurruuurrrrrrddddllulullrrdrddluludldllluurrudllddrrrurrrululrddlldllluurruulurdddllddrrrurruullrrddlldllluurrurrrddludlddruruuulrdddllllluurruuldldddrrrrruuullldlruuldldrurrrrdddluurulllulduuurdlddrrrrdrdrruuuulllllluldrrrrrrrddddlldllllllu",
        "rrruuuulululldrddurluurdrdulldddduuuurrdrddddlldllurrrlluuuuurrdluldddduuurrrddlurrddddluruuululrdrdddllldllurrluuuuurrdrdrdddluruulululldddddrrlluuuuurrdlulddduurrrdrddluluullddduuurrddrrdddluudlldllurrrlluuuuulldrurddduurrddrluullddduuurrddrdrruldddlulldllurrr",
        "drddrruruuuululllddduuurrrdrddddldlluuruulrddlddrruruuuululllddrdduulldrurrddlduruulluurrrdrddddldlluuruulduluurrrdrddddldlllrrruruuuululllddrddduuuluurrrdrddddldldllurdlllurdrruuuulldrurdd",
        "lluurrrrrrurdddrdluuulllllllddrudddldrlddruluuruuulurrrllddddlddruuuuluurrrrrrurddrddllldlldlrurruuldrrrruuldrdlllrruuulllllllddrulurrrrrrurdllllllddddlddruudrrurldlluuuluurrrrrrrdrddllluldrrrruuldrdllruuulllllllddrulurrrrrrurddrddlllrrruuldrdll",
        "lluuudddrrrrdrullllluuuruulddduuurrruullldurrrddlludddlddldrrluuuulurdddddrrrllluuuuruddldddrrrdrrulllldluuuddrrrrrdrruldllulllluuudddrrrrdrruldllullluuuruulldrddddrrrdrrulllldluuuddrrrrrrruulldurrdddlllulllluuuulurdddddrrrrdrrullllldluuuu",
        "lluurddddldrrrrrrurrddlulullllluuuuluuurrdrrdddlulrruullduullddrdddddrrrrrdrruldllulllluuuuuluurrdrrddllrruullullddddrdddldruuuuluurdlddrddrrrrdrrulldllulldrrrrllulluuluurddddrrdrrrurrddluruldllulrdrruldllllulldrrrrlluluuuuuluurrddldddddrdrrulrdrrllllluuuuuururrddllullddrddddrrrrrurrddlululdlllluuuuluurdlddrdddrrrrrrruldlllllluuuluurddduuurrruldllddddldrrluuuuurruuldlddddddrrlluuuuuurrdlullddrddddrrrrrrurdllllllluuuuluurdddddldrrrrrurldrlllurdrrurdlllurrdlllllurrrr",
        "ddldduuruulduruuldulldrrurdddlurdrdddlluuudruuuulldrurdddlurdrduluuulldrdrdrdduululuurdldddddrrrrlllluururdulluurddldddrrrrruurrddldluudllllluuurrdduulldrlddrrrrlllluuuuuulllldrurrrrdddrdllddrrrrruurrddldluudrruullddllllluurrdullddrrrrrllluuuluuulllldrurrrdddrdddrrruurrddldluudrruuuullldrurrddddlluuudddllluuuluuullldrrdrdrdddrrruuulurddddllluuululuurdddldrurddullddrrrrlllddluuudrrrrruurrddldlullllluuuuruulldrurdddldrurdllddrrrrrrruulluurdrdddllllllluurrdullddrrrrruuurulrdrdddldlurruuurulddddlluu",
        "urrdduulldrullldrdddddrrrrrrrrrruuuuulllddllldllurrrrdrruluurrrdddddlllllllllluuuuuurrdlurrrdlddrrrdruudlllluuulldrullldrrldddddrrrrrrrrrruuuuuulllldrrurrddddddlllllllllluuuuuurrrdduulldldddddrrrrrrrrrruuuuuulldlddllldllurrrrdruudlllluuulldrurddullldddddrrrrrrrrrruuuuuulldrullldrrlddllldllurrrrdruu",
        "ululddduurrddlrrddllullllllrrrrrrdrruuluulldduurrddrddllullllrurdlllrrrrrdrruuluullddldlllulllddrrudlluurdrrrrurruurrddrddllulllllllrrrrurruurrddldllllllluldrrrrrrrruruullddrdlulldllulrdrrurrdlllluld",
        "ullllllddlururrrrrrdddlllluulrddrrrruuulllldllrrddrruulrddlluulldllurdrurrrrrllllldllddrrudlluuurrrrrrddrruululrdrruldllllllldlddrruulurrrrlddrruu",
        "urrrdrrulllrruulluurdrdddllluluurrdrluurdlllddduuurrrrdduurrdlulllllddrdrrdrrullllrrruuddlllulldrrrlllddrururrdrulllulldrrrrlluluurrdr",
        "rrurruulllulldrrlldllurrurrddlduruulldldruurrdllrrrrurrdlddlldlluururrllulldrrdldddllurdru",
        "rdrrurdlllulldrrlldllurrurrddlduruulldldruurrdllrrrrddldlluururrurrddlruulldllulldrrdldddllurdrurruru",
        "lulluurrdrrrrdrruuldlllllulldrrrdlurrldlddruluurrrrllllddrululldrurrrrdrddrrruullruuldrrdddllluluurrurrddlullllllldrurrrrdrulllllddrulurrrrdrurrdddlllulur",
        "ddldrdrddluuuurludddruluuulururdlddddrrruuldldluudrrrdlluluuluurrdluldrdddrddlurrrdllulurdddluruuluuddrddluu",
        "drrddlludrruullrrddrrulrdrrulldlllluurrdullddrrrruldlllddrrudlluurrrllluurrdrdllrddlluurruulluldrrrddlludrruullrrdrrrrddlurullldllluurrdrdllrddlluurrurrrdlllddlluuudddrruulrddlluu",
        "uurrrrrdddllullrrddlluluuddrdrruulldlurrrdrruuullulllldrrurrdrrdddlllulludrrddlurrrruuullulldrrulllldrrldddrruldluudlldrruuulurrdlddrrddluruldluudrrrdrrullldlluuururldllurddddrruldluu",
        "rurrdrruuddlllrrruuudddllllldlurrrrrruuuuddddlllllludrruuuururlddrdddrruuuuruuldddddrdlllulldlluuddrruuuururrlldlllulldrrrrddddlluuudddrrurrdllldlurrrrrruuuuruuldddddrdlllulldlluuddrruuuullrrdrrdddrruuuuullldlllulldrrrrddddlluuudddrruurrulrdddrruuuuullldldurdrdddluruululllrrrdrdddllldluudrrurruululllulldrrrrdrrdddlllluuudddrrurruullullrrdrrdddrruuuuullldlllulldrrrrdrlullddddrrrrrruuuuullldlllddddrruudrrulddllluuuurrrddrddllurdru",
        "dlddrruuluulllldddrrurrrddllulurruullddrrrluulllldddrrurrrrlluulllldrurrrddrrrllluulldurrddrddlludrruurrddlluuluulldduurrddrrrurdlllluullddrrrrlddrruurdulllllddrruulllullddrrurrrddllulurrrrrllddrluurrdru",
        "lldlllurdrrurrrddllrruullldlluuurrrrdulllldddrruldllddrrrurrruuluullllddrdluuurrrrddlruullllddlduruurrrrddlllrrrrddllludrrruulllddrruruluullllddldruuurrrrddddlluulrddrruuuulllldduurrrrddllll",
        "uuurrlldddrruululddurrddlllrrrurrullddllllrrrruulrddlllldlurullrrdrrrruululddurrddlluuururdldlddlrrrurulddlllldlurulrdrrrruululddurrddlllrdddlluuluurdrruurrrruldlluurdrdllulddurrddlllluldrrrdddllllluuuluurdrrdrrrdddllllluuulurr",
        "rrrdddrrddrruurululldldluuurullrdddrrurrddddllluuluuuululddrrdddrdrdrruuuulldldlurrurrdlrdddlluluuludrddrdrruuullrrdddllululuuurullrddddrdrdrruuullldluuuruldddrddruddrruuullldluuuludrruldlllluurrdullddrrrrulrdddrrrrdlldluruldluuulurdllllddrrudlluurrlluurrurdlllddrrrllluurrdullddrr",
        "ddrdrrrrurrllddrruldlulllluluurdrrrdulllddrddluurrrllludrrruullldluuuuruldddrdrrrddrdrrullllldluuluuuuulrdddddrdrrruullldluuuuruldddrdrrrddrruldlllluurrrdrdllldluuluuuullluurddrruldlluurdrrdddddrdrrruullldluurdddddllurdrruuluuuluuddrdddrddluurrruullluluulluldrrrrullrdddrdrrrddllludrrruullldluuuuruldddrdrrrdrrdrrruuldllldlllluurrrdrdlurrrrrdllulllullluluullluurdldrrrulrdddrdrrrdrrrdlulldlldluudrrrurrdlulullldluurdrrrdrdluullluludrdrrrddlldluuluuuuddddrdrrruullluluulurdddrddluuuullllddrrudlluurrlurdrllldrdrulur",
        "rddrrururururrdldldldlllrrrururuuldldldldllulldruuulllddddrdrrruurrurururrdldldlllrrruruuldldldllulldrurululllddddrdrrruuluuuurrdldlddrrrururrdldllllddllluluuuurrdrruuldlllddddrdrrruuulldrurudlluullddddrdrrruuulluurdlddrrrrrruuldrdllllddllluluuuurrrurrdluldlllddddrdrrruuuuddddllluluuuurrdddrluuullddddrdrrruulluuururrdllllulddurrdddrruuddlluuullddduuurrrurrdllllulddurrrrdlullldduurrrrdddlluurrdluullulddurrrdddluurulluld",
        "luurudrrddrruulllllruuddrrrrdlullluuluurdrrrdddrddluuuuddllluuddlddlluurrrrrlluuluurdrrdrddrddlludrruulllllrrrruuudddrddluuuuddllllddlluurrrlddlururrrdruudlllldllurrrrluuddrrdrullluulurr",
        "rruldlddrululldrurduludrrurrdlldlluuruulddrrdrruldldluluurdlddrrurulddllurdlllurdrrrddlu",
        "rdrruluullrrdddldlluurrluuuuuuddddddldllurdrdrruuluurrddlddlluluuurrddlruuuuuullluurduluurdrrdddddddduuuuuuuullldddrrluluurrrdddddddllddrddrruuluuuuuuuuruldddlluluurdlddrrruuulrdddddddddrruulrddlluuudllddldruuurrddlruulldduurruuuullluurduluurdrrddddddddurrddrdluuullllddrrrluurrdd",
        null,
        "ddrddrdllduruuuulldrrdddldlurrrrruullddlllulrdrrrrrulldlldlddrruulululudrddddruuluululldrdrlur",
        "dllddrrrruruuulrdddldlurruuuululllldlddrdrruuurldddduuuldlluururdrddlludddlururrddlruurrd",
        "lllllldddrruldluudrrrdrddldlluuluuulurrrllddrrdluluurrrrrlllllddrddlurrrrddluddlluuuluulurrrrrllllddrddddrruurullluluurrrrdrrrruulldurrdddlllulurlllllddrddluuulurrrrrdrdrrulrruulldllrrurrdddlllurrlllullrrdrrrdrullllullllrrrrdrrull",
        "lullllddrdrdrrrruuddrruldludllllululuurrrrdrrurrdllllullllddrdrdrrruruddllllululuurrrrdrrurrdlldddllllululuurrrrddlulullddrdrdrrrrrrrrulrdrruldlllluluullrrddrdrruldlluuullulrdrrdddrrrrruldlllluuulluldrrrdddrruldluudrrdrruldlllllllululuurrdrrulrdrrurrdllllullrrdrrddrdrruldlluuullulldrurdrrdddrruldluuddllllululuurrdrrrrurrdlllllrul",
        "rurdddlddrddldruuuluurddduurlddduuurrrrlllldddlddruuuulurrrddddlruuuulldddlddruudrruuuurlddddlluuddrruuuulluuurrdlulddldrrrddddlluuulurr",
        "lldllluurrdullddrrdrulurrrrllldlllllurdrrrurrurrrrddluruldlllllulldldlllurdrrrrluurrdrrrrruldllllullddlllurdrrrrdrulurdlllllurdrrruulldldrrrurrlluluurdldlddrrurrrrurrddlulllllddrulurdllluururdlldllluurrrdddrrrurldllluurruuldldddrrurrrrllurdrrdru",
        "lullurdruuulrddddldluruuruuluurddddlddllluuururrurdllldldddrrruurdldrlllluuururrrddlldrdrrrrlllluuruulldurrddlddrrrurrrddlullllluuruullddrddrrrrrruldllllluurdldrrrrrlllluullulldddrrlluuurrdrrddrrrrurdllllluullulldddrrrrrrrlurdllluulldrrdrrurrldllluullulldddrrurrdrrurrdllllulldrrrrrr",
        "uurrrrrdrdlllrruullluurrrrdrdrddlruululullllddrrrddlllrrruulllllddlluuurrdrrrrrddllllluurruurrrrdrdrddllrrrruuulululullldurrrdrdrdrdddllllululllllddrrrrruulllllulldddrrrluurruuuullllddrrddrrrrrddrrruulululllrrrdrdrddllulullllluulluurrrrddduuullllddrrddddrrrrruullllruuuullllddrrddrrrrrddrlllllllluuurrdduullurrddrrrrrddllllll",
        null,
        "ruuuuuuuuuuuurrrrrrrrrrrrrrrrrrrrrddddddddddllllllllllllllllluuuuuurrrrrrrrrrrrrddlllllllllllddrrrrrrrrrrrrruuuuuulllllllllllllllllddddddddddrrrrrrrrrrrrrrrrrrrrruuuuuuuuuuuuuulllllllllllllllllllllllllddddddddddddduuuuuuuuuuuuurrrrrrrrrrrrrrrrrrrrrrrrrddddddddddddddllllllllllllllllllllluuuuuuuuuurrrrrrrrrrrrrrrrrddddddllllllllllllluurrrrrrrrrrruulllllllllllllddddddrrrrrrrrrrrrrrrrruuuuuuuuuulllllllllllllllllllllddddddddddddll",
        "uulldrrrrrrrrdruuurulllllllllllluldddrdlllllllllllulllddrrudlluurrluurrddldrrrrrrrrrrdruuulurrrrrrrrrdruuuuuurullluldddrdllldluuurullluldddrdlllldluuuudrruulllrddlluuulurrrrrrrrrrrrrrrrrrrurddddddlddruuuuuurulllllllllllllllllllluldrdlllllulldrurdddddrddllurdrulurrurddddrrddlluuuuuu"
    };

    private static String decodeXsb() {
        byte[] compressed = Base64.getDecoder().decode(XSB_B64);
        try (InflaterInputStream inf = new InflaterInputStream(new ByteArrayInputStream(compressed))) {
            return new String(readAllBytes(inf), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("解压内嵌 Microban 关卡失败", e);
        }
    }

    /** Java 8 兼容：InputStream.readAllBytes() 自 Java 9 才有。 */
    private static byte[] readAllBytes(java.io.InputStream in) throws IOException {
        byte[] buf = new byte[4096];
        int n;
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        while ((n = in.read(buf)) != -1) {
            out.write(buf, 0, n);
        }
        return out.toByteArray();
    }
}
