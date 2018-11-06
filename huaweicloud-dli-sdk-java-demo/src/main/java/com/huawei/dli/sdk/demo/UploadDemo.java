package com.huawei.dli.sdk.demo;

import com.huawei.dli.sdk.*;
import com.huawei.dli.sdk.authentication.AuthenticationMode;
import com.huawei.dli.sdk.common.*;
import com.huawei.dli.sdk.demo.utils.DemoConf;
import com.huawei.dli.sdk.exception.DLIException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class UploadDemo {
  static String ak = DemoConf.getInstance().get("ak");
  static String sk = DemoConf.getInstance().get("sk");
  static String region = DemoConf.getInstance().get("region");
  static String project = DemoConf.getInstance().get("project");
  static String db = "sdk_db";

  public static void main(String[] args) {
    DLIClient client = akskAuth();
    Table table = createTable(client, db);
    if (table == null) {
      return;
    }
    uploadData(client, table);
    runSqlWithResult(client, db, "select * from tbl_upload");
    dropTable(table);
  }

  private static void dropTable(Table table) {
    try {
      table.deleteTable();
    } catch (DLIException e) {
      e.printStackTrace();
    }
  }

  private static void uploadData(DLIClient client, Table table) {
    try {
      UploadJob job = new UploadJob(client.getQueue("queue_for_demo"), db, "tbl_upload");
      Writer writer = job.createWriter();
      for (int i = 1; i < 10; i++) {
        Row row = job.newRow();
        row.setString(0,"[{\"course\":\"Mathematics\",\"score\":80}, " +
            "{\"course\":\"Chinese\",\"score\":90}]");
        row.setString(1, "[\"Mathematics\", \"English\", \"Chinese\"]");
        writer.write(row);
      }
      writer.flush();
      writer.close();
      job.beginCommit();
      while (!(job.getCommitStatus().equals(JobStatus.FAILED)
              || job.getCommitStatus().equals(JobStatus.FINISHED))) {
        sleep(1000);
      }
    } catch (DLIException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static Table createTable(DLIClient client, String db) {
    List<Column> columns = new ArrayList<Column>();
    Column c1 = new Column("id", DataType.STRING);
    Column c2 = new Column("name", DataType.STRING);
    columns.add(c1);
    columns.add(c2);
    Table table = null;
    try {
      table = client.getDatabase(db).createDLITable("tbl_upload", "", columns, null);
    } catch (DLIException e) {
      e.printStackTrace();
    }
    return table;
  }

  private static DLIClient akskAuth() {
    DLIInfo info = new DLIInfo(region, ak, sk, project);
    return new DLIClient(AuthenticationMode.AKSK, info);
  }

  private static void runSqlWithResult(DLIClient client, String db, String sql) {
    try {
      SQLJob job = new SQLJob(client.getQueue("queue_for_demo"), db, sql);
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
        System.out.println(rs.getRow().getString(0) + "," +rs.getRow().getString(1));
      }
      ++idx;
    }
  }
}
