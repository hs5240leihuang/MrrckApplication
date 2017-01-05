package com.meiku.dev.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.CursorUtils;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.meiku.dev.MrrckApplication;
import com.meiku.dev.bean.AreaEntity;
import com.meiku.dev.bean.DataconfigEntity;
import com.meiku.dev.bean.GroupClassEntity;
import com.meiku.dev.bean.JobClassEntity;
import com.meiku.dev.bean.MkDataConfigPlan;
import com.meiku.dev.bean.MkDataConfigReleaseMonths;
import com.meiku.dev.bean.MkDataConfigTopPrice;
import com.meiku.dev.bean.MkDecorateCategory;
import com.meiku.dev.bean.MkMenu;
import com.meiku.dev.bean.MkPostsActiveCategory;
import com.meiku.dev.bean.MkProductCategory;
import com.meiku.dev.bean.ReportType;
import com.meiku.dev.utils.FileConstant;

/**
 * Created by mrrck on 15/6/17. 数据库处理相关业务逻辑
 */
public class MKDataBase {
	private DbUtils dataBase;

	// 私有的默认构造子
	private MKDataBase() {
	}

	// 已经自行实例化
	private static MKDataBase single = new MKDataBase();

	// 静态工厂方法
	public static MKDataBase getInstance() {
		return single;
	}

	/**
	 * 获取DB
	 * 
	 * @return DbUtils
	 */
	public DbUtils getDB() {
		if (single == null) {
			single = new MKDataBase();
		}
		if (single.dataBase == null) {
			initDataBase(MrrckApplication.getInstance(),
					FileConstant.LOCALDB_PATH, FileConstant.DB_NAME);
		}
		// 标示开启事务，这样多个线程操作数据库时就不会出现问题了。
		single.dataBase.configAllowTransaction(true);
		// DaoConfig config = new DaoConfig(context);
		// config.setDbName("xUtils-demo"); //db名
		return single.dataBase;
	}

	/**
	 * 初始化DB
	 * 
	 * @param dbPath
	 *            数据库Path
	 * @param dbName
	 *            数据库名称
	 */
	private void initDataBase(Context context, String dbPath, String dbName) {
		single.dataBase = DbUtils.create(context, dbPath, dbName);
	}

	// private void initDataBase(Context context) {
	// MKDataBase.getInstance().dataBase = DbUtils.create(context);
	//
	// }

	//
	// db.execSQL("CREATE TABLE [im_msg_his] ([_id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
	// +
	// "[content] TEXT, [msg_from] NVARCHAR, [msg_to] NVARCHAR, [msg_time] TEXT, [headimg] TEXT,"
	// +
	// "[type] INTEGER,[path] TEXT,[msg_type] INTEGER);");
	//
	// db.execSQL("CREATE TABLE [im_notice]  ([_id] INTEGER NOT NULL  PRIMARY KEY AUTOINCREMENT,"
	// +
	// " [type] INTEGER, [title] NVARCHAR, [content] TEXT, [notice_from] NVARCHAR,"
	// +
	// " [notice_to] NVARCHAR, [notice_time] TEXT,[headimg] TEXT, [status] INTEGER);");
	/**
	 * 获取库群
	 * 
	 * @return Array
	 */
	public List<GroupClassEntity> getGroupClass() {
		List<GroupClassEntity> resultList = new ArrayList<GroupClassEntity>();
		String sql = "select * from mk_library_group  ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				GroupClassEntity jobClassDTO = new GroupClassEntity(model);

				resultList.add(jobClassDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取岗位一级类
	 * 
	 * @return Array
	 */
	public List<JobClassEntity> getJobClass() {
		List<JobClassEntity> resultList = new ArrayList<JobClassEntity>();
		String sql = "select * from mk_position where pid = 0 order by sortno";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				JobClassEntity jobClassDTO = new JobClassEntity(model);

				resultList.add(jobClassDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取岗位一级类 不包含厂商代理商和店家经营者
	 * 
	 * @return Array
	 */
	public List<JobClassEntity> getJobNobossClassIntent() {
		List<JobClassEntity> resultList = new ArrayList<JobClassEntity>();
		String sql = "select * from mk_position where pid = 0 and name != '厂家代理商' and name != '店家经营者'  order by sortno";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				JobClassEntity jobClassDTO = new JobClassEntity(model);

				resultList.add(jobClassDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取岗位一级类
	 * 
	 * @return Array
	 */
	public List<JobClassEntity> getJobClassIntent() {
		List<JobClassEntity> resultList = new ArrayList<JobClassEntity>();
		String sql = "select * from mk_position where pid = 0   order by sortno";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				JobClassEntity jobClassDTO = new JobClassEntity(model);

				resultList.add(jobClassDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取找工作岗位一级类
	 * 
	 * @return Array
	 */
	public List<JobClassEntity> getFindJobPositionList() {
		List<JobClassEntity> resultList = new ArrayList<JobClassEntity>();
		String sql = "select * from mk_position where pid = 0 and jobFlag = 1  order by sortno";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				JobClassEntity jobClassDTO = new JobClassEntity(model);

				resultList.add(jobClassDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取岗位二级分类
	 * 
	 * @param jobClassId
	 *            岗位ID
	 * @return 返回岗位列表
	 */
	public List<JobClassEntity> getJobSubClass(int jobClassId) {
		List<JobClassEntity> resultList = new ArrayList<JobClassEntity>();
		String sql = "select * from mk_position where pid = " + jobClassId
				+ " order by sortno";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				JobClassEntity jobClassDTO = new JobClassEntity(model);

				resultList.add(jobClassDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 通过二级岗位ID获取一级岗位ID
	 * 
	 * @return Array
	 */
	public int getFirstJobIDByPositionId(int position) {
		int jobId = -1;
		String sql = "select groupId from mk_position where id = " + position;
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				jobId = model.getInt("groupId");
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobId;
	}

	/**
	 * 通过岗位ID获取岗位名称
	 * 
	 * @return Array
	 */
	public String getJobNameById(int id) {
		String jobName = "";
		String sql = "select name from mk_position where id = " + id;
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				jobName = model.getString("name");
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobName;
	}

	/**
	 * 获取热门城市
	 * 
	 * @return
	 */
	public List<AreaEntity> getHotCity() {
		List<AreaEntity> resultList = new ArrayList<AreaEntity>();
		String sql = "select * from mk_area where isHot = 1";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				AreaEntity areaDTO = new AreaEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取省或直辖市
	 * 
	 * @return
	 */
	public List<AreaEntity> getCity() {
		List<AreaEntity> resultList = new ArrayList<AreaEntity>();
		String sql = "select * from mk_area where level = 1";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				AreaEntity areaDTO = new AreaEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 根据直辖市CODE获取对应名称映射
	 */
	public Map<Integer, String> getMunCityCode() {
		Map<Integer, String> map = new HashMap<Integer, String>();

		String sql = "select cityName,cityCode from mk_area where level = 1 And cityCode in (110000,120000,310000,500000,710000,810000,820000)";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				map.put(model.getInt("cityCode"), model.getString("cityName"));
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 获取全部市或直辖市
	 * 
	 * @return
	 */
	public List<AreaEntity> getAllCity() {
		List<AreaEntity> resultList = new ArrayList<AreaEntity>();
		String sql = "select * from mk_area where level = 2 or cityCode in (110000,120000,310000,500000,710000,810000,820000)";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				AreaEntity areaDTO = new AreaEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 根据省code获取全部市
	 * 
	 * @return
	 */
	public List<AreaEntity> getAllCityByProvinceCode(String provinceCode) {
		List<AreaEntity> resultList = new ArrayList<AreaEntity>();
		String sql = "select * from mk_area where (parentCode in ("
				+ provinceCode
				+ ") and level = 2) or (cityCode in ("
				+ provinceCode
				+ ") and cityCode in (110000,120000,310000,500000,710000,810000,820000))";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				AreaEntity areaDTO = new AreaEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 根据省code获取全部市
	 * 
	 * @return
	 */
	public List<AreaEntity> getAllCityByCityCode(String cityCode) {
		List<AreaEntity> resultList = new ArrayList<AreaEntity>();
		String sql = "select * from mk_area where cityCode in (" + cityCode
				+ ")";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				AreaEntity areaDTO = new AreaEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (DbException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取省市
	 * 
	 * @return
	 */
	public String getCityNameByCityCode(int cityCode) {
		// List<AreaEntity> resultList = new ArrayList<AreaEntity>();
		String cityName = null;
		String sql = "select cityName from mk_area where cityCode = "
				+ cityCode;
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				cityName = model.getString("cityName");
				// cityName = new AreaEntity(model);
				// resultList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cityName;
	}

	/**
	 * 获取城市下的区
	 * 
	 * @return
	 */
	public List<AreaEntity> getDistrict(int parentCode) {
		List<AreaEntity> resultList = new ArrayList<AreaEntity>();
		String sql = "select * from mk_area where parentCode = " + parentCode;
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				AreaEntity areaDTO = new AreaEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
     *
     */
	/**
	 * 获取薪水
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getSalary() {
		List<DataconfigEntity> resultList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'EXPECT_SALARY' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取公司规模列表
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getCompanyScale() {
		List<DataconfigEntity> resultList = new ArrayList<DataconfigEntity>();

		String sql = "select * from  mk_data_config where code = 'COMPANY_SCALE'";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultList;
	}

	/**
	 * 根据codeid获取数据
	 * 
	 * @return
	 */
	public DataconfigEntity getDataByCodeId(String codeId) {

		DataconfigEntity areaDTO = null;

		String sql = "select * from  mk_data_config where codeId = '" + codeId
				+ "' limit 0,1";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				areaDTO = new DataconfigEntity(model);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return areaDTO;
	}

	/**
	 * 根据codeid获取mk_data_config表中的value
	 * 
	 * @return
	 */
	public String getValueByCodeId(String codeId) {
		DataconfigEntity entity = getDataByCodeId(codeId);
		if (entity == null) {
			return "";
		}
		return entity.getValue();
	}

	/**
	 * 根据id获取数据
	 * 
	 * @return
	 */
	public DataconfigEntity getDataById(String id) {
		DataconfigEntity areaDTO = null;

		String sql = "select * from  mk_data_config where id = '" + id
				+ "' limit 0,1";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				areaDTO = new DataconfigEntity(model);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return areaDTO;
	}

	/**
     *
     */
	/**
	 * 工作年限
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getWorkAge() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'JOB_AGE' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 获取配置信息
	 * 
	 * @return
	 **/
	public List<DataconfigEntity> getDataConfigList(String code) {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = '" + code
				+ "'";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 专业技法
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getWorkSkill() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'PROFESSION_KNOWLEDGE' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 福利待遇
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getBenefits() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'FRINGE_BENEFITS' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 需要人数
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getNumberNeeds() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'JOB_NUMBER_REQUIRE' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 老板类型
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getBossTypes() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'BOSS_TYPE' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 年龄
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getAge() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'AGE_REQUIRE' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 学历
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getEdution() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'EDUCATION_REQUIRE' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 生活特长
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getLifeSkill() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'GOOD_AT' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 公司类型
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getCompanyType() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'COMPANY_TYPE' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 工作性质
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getWorkType() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'WORK_TYPE' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
	 * 举报类型
	 * 
	 * @return
	 */
	public List<ReportType> getReportType() {
		List<ReportType> reportTypeList = new ArrayList<ReportType>();
		String sql = "select * from  mk_report_type where delStatus = '0' ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				ReportType areaDTO = new ReportType(model);
				reportTypeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reportTypeList;
	}

	/**
	 * 到岗时间
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getArriveETme() {
		List<DataconfigEntity> WorkAgeList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = 'ARRIVE_TIME' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				WorkAgeList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WorkAgeList;
	}

	/**
     *
     */
	/**
	 * 根据code获取数据
	 * 
	 * @return
	 */
	public List<DataconfigEntity> getDataByCode(String code) {
		List<DataconfigEntity> resultList = new ArrayList<DataconfigEntity>();
		String sql = "select * from  mk_data_config where code = '" + code
				+ "' ORDER BY sortNo ASC ";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				DataconfigEntity areaDTO = new DataconfigEntity(model);
				resultList.add(areaDTO);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取cityCode
	 * 
	 * @return
	 */
	public String getCityCode(String cityName) {
		String cityCode = "";
		String sql = "select cityCode from mk_area where cityName = '"
				+ cityName + "'";
		try {
			Cursor cursor = getDB().execQuery(sql);
			if (cursor.moveToFirst()) {
				cityCode = cursor.getString(0);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cityCode;
	}

	/**
	 * 获取provinceCode
	 * 
	 * @return
	 */
	public String getprovinceCodeBycityCode(String cityCode) {
		String provinceCode = "";
		String sql = "select parentCode from mk_area where cityCode = '"
				+ cityCode + "'";
		try {
			Cursor cursor = getDB().execQuery(sql);
			if (cursor.moveToFirst()) {
				provinceCode = cursor.getString(0);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return provinceCode;
	}

	/**
	 * 获取
	 * 
	 * @return Array
	 */
	public List<MkMenu> getTitleTabs() {
		List<MkMenu> resultList = new ArrayList<MkMenu>();
		String sql = "select * from mk_menu ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				MkMenu dto = new MkMenu(model);

				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
		return resultList;
	}

	/**
	 * 获取秀场作品分类
	 * 
	 * @return Array
	 */
	public List<MkPostsActiveCategory> getShowCategoryTabs() {
		List<MkPostsActiveCategory> resultList = new ArrayList<MkPostsActiveCategory>();
		String sql = "select * from mk_posts_active_category  ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				MkPostsActiveCategory dto = new MkPostsActiveCategory(model);

				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取赛事作品分类
	 * 
	 * @return Array
	 */
	public List<MkPostsActiveCategory> getMatchCategoryTabs() {
		List<MkPostsActiveCategory> resultList = new ArrayList<MkPostsActiveCategory>();
		String sql = "select * from mk_posts_active_category where matchFlag = 1 ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				MkPostsActiveCategory dto = new MkPostsActiveCategory(model);

				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 根据categoryId获取秀场作品分类
	 * 
	 * @return Array
	 */
	public List<MkPostsActiveCategory> getShowCategoryTabsByCategories(
			String categoryIds) {
		List<MkPostsActiveCategory> resultList = new ArrayList<MkPostsActiveCategory>();
		String sql = "select * from mk_posts_active_category where id in ("
				+ categoryIds + ") and delStatus = 0 order by sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				MkPostsActiveCategory dto = new MkPostsActiveCategory(model);

				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取找产品分类
	 * 
	 * @return Array
	 */
	public List<MkProductCategory> getProdectCategory() {
		List<MkProductCategory> resultList = new ArrayList<MkProductCategory>();
		String sql = "select * from mk_product_category";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {

				DbModel model = CursorUtils.getDbModel(cursor);

				MkProductCategory dto = new MkProductCategory(model);

				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 获取是否全省下，产品单价
	 * 
	 * @param isWhole
	 *            0=单一省份单价，1=是全部省份价格
	 * @return
	 */
	public String getProdectPrice(int isWhole) {
		String perAmount = "";
		String sql = "select perAmount from mk_product_amount where isWhole= '"
				+ isWhole + "'";
		try {
			Cursor cursor = getDB().execQuery(sql);
			if (cursor.moveToFirst()) {
				perAmount = cursor.getString(0);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return perAmount;
	}

	/**
	 * type=获取装修参数 0 店铺分类 1材料费类型 2 人工费类型 3 设计费类型 showFlag =0，1全部类型
	 * 
	 * @return Array
	 */
	public List<MkDecorateCategory> getDecorateCategoryList(int type,
			int showFlag) {
		List<MkDecorateCategory> resultList = new ArrayList<MkDecorateCategory>();
		String sql = "select * from mk_decorate_category where type = " + type
				+ " and showFlag = " + showFlag + " ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				MkDecorateCategory dto = new MkDecorateCategory(model);
				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
		return resultList;
	}

	/**
	 * type=指标类型 0 店铺分类 1材料费类型 2 人工费类型 3 设计费类型 4 评价等级 5装修费用预算 6 装修面积指标
	 * 
	 * @return Array
	 */
	public List<MkDecorateCategory> getDecorateCategoryList(int type) {
		List<MkDecorateCategory> resultList = new ArrayList<MkDecorateCategory>();
		String sql = "select * from mk_decorate_category where type = " + type
				+ " ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				MkDecorateCategory dto = new MkDecorateCategory(model);
				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
		return resultList;
	}

	/**
	 * 0 找装修置顶置顶费用 1找产品置顶费用，2购买提示文字,3招聘宝购买提示文字
	 * 
	 * @return Array
	 */
	public List<MkDataConfigTopPrice> getTopPriceList(int type) {
		List<MkDataConfigTopPrice> resultList = new ArrayList<MkDataConfigTopPrice>();
		String sql = "select * from mk_data_config_top_price where type = "
				+ type + " ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				MkDataConfigTopPrice dto = new MkDataConfigTopPrice(model);
				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
		return resultList;
	}

	/**
	 * 获取购买月数type 0装修购买，1找产品购买 2购买vip,3招聘宝公司h5url地址
	 * 
	 * @return Array
	 */
	public List<MkDataConfigReleaseMonths> getReleaseMonthsList(int type) {
		List<MkDataConfigReleaseMonths> resultList = new ArrayList<MkDataConfigReleaseMonths>();
		String sql = "select * from mk_data_config_release_months where type="
				+ type + " ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				MkDataConfigReleaseMonths dto = new MkDataConfigReleaseMonths(
						model);
				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
		return resultList;
	}

	/**
	 * type=找策划首页菜单 0 ，店铺类型1 functionLayout = 1234种样式
	 * 
	 * @return Array
	 */
	public List<MkDataConfigPlan> getPlanMenuList(int type, int functionLayout) {
		List<MkDataConfigPlan> resultList = new ArrayList<MkDataConfigPlan>();
		String sql = "select * from mk_data_config_plan where type = " + type
				+ " and functionLayout  =" + functionLayout
				+ " ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				MkDataConfigPlan dto = new MkDataConfigPlan(model);
				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
		return resultList;
	}

	/**
	 * type=找策划首页菜单 0 ，店铺类型1
	 */
	public List<MkDataConfigPlan> getPlanShopList(int type) {
		List<MkDataConfigPlan> resultList = new ArrayList<MkDataConfigPlan>();
		String sql = "select * from mk_data_config_plan where type = " + type
				+ " ORDER BY sortNo ASC";
		try {
			Cursor cursor = getDB().execQuery(sql);
			while (cursor.moveToNext()) {
				DbModel model = CursorUtils.getDbModel(cursor);
				MkDataConfigPlan dto = new MkDataConfigPlan(model);
				resultList.add(dto);
			}
			cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
			return resultList;
		}
		return resultList;
	}
}
