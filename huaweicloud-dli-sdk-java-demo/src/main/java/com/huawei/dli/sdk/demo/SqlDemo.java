package com.huawei.dli.sdk.demo;

import com.huawei.dli.sdk.DLIClient;
import com.huawei.dli.sdk.ResultSet;
import com.huawei.dli.sdk.SQLJob;
import com.huawei.dli.sdk.authentication.AuthenticationMode;
import com.huawei.dli.sdk.common.DLIInfo;
import com.huawei.dli.sdk.demo.utils.DemoConf;
import com.huawei.dli.sdk.exception.DLIException;

public class SqlDemo {
  static String ak = DemoConf.getInstance().get("ak");
  static String sk = DemoConf.getInstance().get("sk");
  static String region = DemoConf.getInstance().get("region");
  static String project = DemoConf.getInstance().get("project");

  public static void main(String[] args) {
    DLIClient client = akskAuth();
    runSqlWithResult(client, "sdk_db", "select * from tbl_info");
  }

  private static DLIClient akskAuth() {
    DLIInfo info = new DLIInfo(region, ak, sk, project);
    return new DLIClient(AuthenticationMode.AKSK, info);
  }

  private static void runSqlWithResult(DLIClient client, String db, String sql) {
    try {
      SQLJob job = new SQLJob(client.getDefaultQueue(), db, sql);
      ResultSet rs = job.submitQuery();
      printResult(rs);
    } catch (DLIException e) {
      e.printStackTrace();
    }
  }

  private static void printResult(ResultSet rs) throws DLIException {
    System.out.println(rs.getRowCount());
    System.out.println(rs.getSchema().getColumns());

    int idx = 0;
    while (rs.next()) {
      if (idx < 10) {
        System.out.print(rs.getRow().getCsvString());
      }
      ++idx;
    }
  }
}
