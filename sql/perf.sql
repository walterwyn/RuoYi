-- ----------------------------------------------------------
-- 员工绩效管理（ruoyi-perf）
-- 在已执行 ry_xxxx.sql 的数据库上增量执行
-- ----------------------------------------------------------

-- ----------------------------
-- 1、考核指标表
-- ----------------------------
drop table if exists perf_indicator;
create table perf_indicator (
  indicator_id      bigint(20)      not null auto_increment    comment '指标ID',
  indicator_name    varchar(100)    not null                   comment '指标名称',
  indicator_type    char(1)         default '1'                comment '指标类型（1业绩指标 2能力指标 3态度指标）',
  scoring_standard  varchar(500)    default ''                 comment '评分标准',
  order_num         int(4)          default 0                  comment '显示顺序',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (indicator_id)
) engine=innodb auto_increment=100 comment = '考核指标表';

insert into perf_indicator values(1, '工作目标完成度', '1', '100分：超额完成全部目标；80分：完成全部目标；60分：完成大部分目标；60分以下：未完成主要目标', 1, '0', 'admin', sysdate(), '', null, null);
insert into perf_indicator values(2, '工作质量',       '1', '100分：成果零差错并可作为标杆；80分：偶有小问题且及时修正；60分：问题较多需返工', 2, '0', 'admin', sysdate(), '', null, null);
insert into perf_indicator values(3, '工作效率',       '1', '100分：总是提前完成；80分：按期完成；60分：偶有延期；60分以下：经常延期', 3, '0', 'admin', sysdate(), '', null, null);
insert into perf_indicator values(4, '专业能力',       '2', '能够独立解决本岗位复杂问题并指导他人为优秀', 4, '0', 'admin', sysdate(), '', null, null);
insert into perf_indicator values(5, '学习与创新',     '2', '主动学习新知识并提出可落地的改进建议为优秀', 5, '0', 'admin', sysdate(), '', null, null);
insert into perf_indicator values(6, '团队协作',       '3', '主动配合、乐于分享、无推诿为优秀', 6, '0', 'admin', sysdate(), '', null, null);
insert into perf_indicator values(7, '责任心与纪律',   '3', '认真负责、遵守制度、无违纪记录为优秀', 7, '0', 'admin', sysdate(), '', null, null);


-- ----------------------------
-- 2、考核模板表
-- ----------------------------
drop table if exists perf_template;
create table perf_template (
  template_id       bigint(20)      not null auto_increment    comment '模板ID',
  template_name     varchar(100)    not null                   comment '模板名称',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (template_id)
) engine=innodb auto_increment=100 comment = '考核模板表';

insert into perf_template values(1, '通用岗位考核模板', '0', 'admin', sysdate(), '', null, '适用于大多数职能岗位');


-- ----------------------------
-- 3、考核模板指标表
-- ----------------------------
drop table if exists perf_template_item;
create table perf_template_item (
  item_id           bigint(20)      not null auto_increment    comment '主键',
  template_id       bigint(20)      not null                   comment '模板ID',
  indicator_id      bigint(20)      not null                   comment '指标ID',
  weight            int(3)          not null                   comment '权重（%）',
  order_num         int(4)          default 0                  comment '显示顺序',
  primary key (item_id),
  key idx_template_id (template_id)
) engine=innodb auto_increment=100 comment = '考核模板指标表';

insert into perf_template_item values(1, 1, 1, 30, 1);
insert into perf_template_item values(2, 1, 2, 20, 2);
insert into perf_template_item values(3, 1, 3, 15, 3);
insert into perf_template_item values(4, 1, 4, 15, 4);
insert into perf_template_item values(5, 1, 5, 5,  5);
insert into perf_template_item values(6, 1, 6, 10, 6);
insert into perf_template_item values(7, 1, 7, 5,  7);


-- ----------------------------
-- 4、考核计划表
-- ----------------------------
drop table if exists perf_plan;
create table perf_plan (
  plan_id           bigint(20)      not null auto_increment    comment '计划ID',
  plan_name         varchar(100)    not null                   comment '计划名称',
  cycle_type        char(1)         default '2'                comment '周期类型（1月度 2季度 3半年度 4年度）',
  start_date        date                                       comment '考核开始日期',
  end_date          date                                       comment '考核结束日期',
  template_id       bigint(20)      not null                   comment '考核模板ID',
  self_weight       int(3)          default 20                 comment '自评权重（%）',
  plan_status       char(1)         default '0'                comment '计划状态（0未发布 1进行中 2已结束）',
  publish_time      datetime                                   comment '发布时间',
  finish_time       datetime                                   comment '结束时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (plan_id)
) engine=innodb auto_increment=100 comment = '考核计划表';


-- ----------------------------
-- 5、员工考核单表
-- ----------------------------
drop table if exists perf_appraisal;
create table perf_appraisal (
  appraisal_id      bigint(20)      not null auto_increment    comment '考核单ID',
  plan_id           bigint(20)      not null                   comment '计划ID',
  user_id           bigint(20)      not null                   comment '被考核人ID',
  dept_id           bigint(20)      default null               comment '被考核人部门ID',
  reviewer_id       bigint(20)      default null               comment '评分人ID',
  self_score        decimal(5,2)    default null               comment '自评得分',
  leader_score      decimal(5,2)    default null               comment '上级评分',
  final_score       decimal(5,2)    default null               comment '最终得分',
  grade             char(1)         default null               comment '绩效等级（S A B C D）',
  self_comment      varchar(1000)   default ''                 comment '自评总结',
  leader_comment    varchar(1000)   default ''                 comment '上级评语',
  appraisal_status  char(1)         default '0'                comment '考核状态（0待自评 1待评分 2已完成）',
  self_time         datetime                                   comment '自评提交时间',
  review_time       datetime                                   comment '评分时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (appraisal_id),
  unique key uk_plan_user (plan_id, user_id),
  key idx_reviewer_id (reviewer_id),
  key idx_user_id (user_id)
) engine=innodb auto_increment=100 comment = '员工考核单表';


-- ----------------------------
-- 6、考核单明细表
-- ----------------------------
drop table if exists perf_appraisal_item;
create table perf_appraisal_item (
  item_id           bigint(20)      not null auto_increment    comment '主键',
  appraisal_id      bigint(20)      not null                   comment '考核单ID',
  indicator_id      bigint(20)      default null               comment '指标ID',
  indicator_name    varchar(100)    default ''                 comment '指标名称',
  indicator_type    char(1)         default ''                 comment '指标类型',
  scoring_standard  varchar(500)    default ''                 comment '评分标准',
  weight            int(3)          not null                   comment '权重（%）',
  self_score        decimal(5,2)    default null               comment '自评分',
  leader_score      decimal(5,2)    default null               comment '上级评分',
  order_num         int(4)          default 0                  comment '显示顺序',
  primary key (item_id),
  key idx_appraisal_id (appraisal_id)
) engine=innodb auto_increment=100 comment = '考核单明细表';


-- ----------------------------
-- 7、数据字典
-- ----------------------------
delete from sys_dict_data where dict_type in ('perf_indicator_type', 'perf_cycle_type', 'perf_plan_status', 'perf_appraisal_status', 'perf_grade');
delete from sys_dict_type where dict_type in ('perf_indicator_type', 'perf_cycle_type', 'perf_plan_status', 'perf_appraisal_status', 'perf_grade');

insert into sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark) values
('绩效指标类型', 'perf_indicator_type',   '0', 'admin', sysdate(), '绩效指标类型列表'),
('考核周期',     'perf_cycle_type',       '0', 'admin', sysdate(), '绩效考核周期列表'),
('考核计划状态', 'perf_plan_status',      '0', 'admin', sysdate(), '绩效考核计划状态列表'),
('考核单状态',   'perf_appraisal_status', '0', 'admin', sysdate(), '员工考核单状态列表'),
('绩效等级',     'perf_grade',            '0', 'admin', sysdate(), '绩效等级列表');

insert into sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) values
(1, '业绩指标', '1', 'perf_indicator_type',   '', 'primary', 'Y', '0', 'admin', sysdate(), '业绩类指标'),
(2, '能力指标', '2', 'perf_indicator_type',   '', 'info',    'N', '0', 'admin', sysdate(), '能力类指标'),
(3, '态度指标', '3', 'perf_indicator_type',   '', 'warning', 'N', '0', 'admin', sysdate(), '态度类指标'),
(1, '月度',     '1', 'perf_cycle_type',       '', '',        'N', '0', 'admin', sysdate(), '月度考核'),
(2, '季度',     '2', 'perf_cycle_type',       '', '',        'Y', '0', 'admin', sysdate(), '季度考核'),
(3, '半年度',   '3', 'perf_cycle_type',       '', '',        'N', '0', 'admin', sysdate(), '半年度考核'),
(4, '年度',     '4', 'perf_cycle_type',       '', '',        'N', '0', 'admin', sysdate(), '年度考核'),
(1, '未发布',   '0', 'perf_plan_status',      '', 'default', 'Y', '0', 'admin', sysdate(), '计划未发布'),
(2, '进行中',   '1', 'perf_plan_status',      '', 'primary', 'N', '0', 'admin', sysdate(), '计划进行中'),
(3, '已结束',   '2', 'perf_plan_status',      '', 'success', 'N', '0', 'admin', sysdate(), '计划已结束'),
(1, '待自评',   '0', 'perf_appraisal_status', '', 'warning', 'Y', '0', 'admin', sysdate(), '等待员工自评'),
(2, '待评分',   '1', 'perf_appraisal_status', '', 'info',    'N', '0', 'admin', sysdate(), '等待上级评分'),
(3, '已完成',   '2', 'perf_appraisal_status', '', 'success', 'N', '0', 'admin', sysdate(), '考核已完成'),
(1, 'S',        'S', 'perf_grade',            '', 'success', 'N', '0', 'admin', sysdate(), '卓越（90分及以上）'),
(2, 'A',        'A', 'perf_grade',            '', 'primary', 'N', '0', 'admin', sysdate(), '优秀（80-89分）'),
(3, 'B',        'B', 'perf_grade',            '', 'info',    'N', '0', 'admin', sysdate(), '良好（70-79分）'),
(4, 'C',        'C', 'perf_grade',            '', 'warning', 'N', '0', 'admin', sysdate(), '合格（60-69分）'),
(5, 'D',        'D', 'perf_grade',            '', 'danger',  'N', '0', 'admin', sysdate(), '待改进（60分以下）');


-- ----------------------------
-- 8、菜单与按钮权限
-- ----------------------------
delete from sys_role_menu where menu_id in (select menu_id from sys_menu where perms like 'perf:%' or (menu_name = '绩效管理' and menu_type = 'M' and parent_id = 0));
delete from sys_menu where perms like 'perf:%';
delete from sys_menu where menu_name = '绩效管理' and menu_type = 'M' and parent_id = 0;

insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
values('绩效管理', '0', '5', '#', '', 'M', '0', '1', '', 'fa fa-line-chart', 'admin', sysdate(), '绩效管理目录');
select @perfRoot := LAST_INSERT_ID();

-- 指标库
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
values('指标库', @perfRoot, '1', '/perf/indicator', '', 'C', '0', '1', 'perf:indicator:view', 'fa fa-tags', 'admin', sysdate(), '考核指标菜单');
select @parentId := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time) values
('指标查询', @parentId, '1', '#', '', 'F', '0', '1', 'perf:indicator:list',   '#', 'admin', sysdate()),
('指标新增', @parentId, '2', '#', '', 'F', '0', '1', 'perf:indicator:add',    '#', 'admin', sysdate()),
('指标修改', @parentId, '3', '#', '', 'F', '0', '1', 'perf:indicator:edit',   '#', 'admin', sysdate()),
('指标删除', @parentId, '4', '#', '', 'F', '0', '1', 'perf:indicator:remove', '#', 'admin', sysdate()),
('指标导出', @parentId, '5', '#', '', 'F', '0', '1', 'perf:indicator:export', '#', 'admin', sysdate());

-- 考核模板
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
values('考核模板', @perfRoot, '2', '/perf/template', '', 'C', '0', '1', 'perf:template:view', 'fa fa-clone', 'admin', sysdate(), '考核模板菜单');
select @parentId := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time) values
('模板查询', @parentId, '1', '#', '', 'F', '0', '1', 'perf:template:list',   '#', 'admin', sysdate()),
('模板新增', @parentId, '2', '#', '', 'F', '0', '1', 'perf:template:add',    '#', 'admin', sysdate()),
('模板修改', @parentId, '3', '#', '', 'F', '0', '1', 'perf:template:edit',   '#', 'admin', sysdate()),
('模板删除', @parentId, '4', '#', '', 'F', '0', '1', 'perf:template:remove', '#', 'admin', sysdate());

-- 考核计划
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
values('考核计划', @perfRoot, '3', '/perf/plan', '', 'C', '0', '1', 'perf:plan:view', 'fa fa-calendar-check-o', 'admin', sysdate(), '考核计划菜单');
select @parentId := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time) values
('计划查询', @parentId, '1', '#', '', 'F', '0', '1', 'perf:plan:list',    '#', 'admin', sysdate()),
('计划新增', @parentId, '2', '#', '', 'F', '0', '1', 'perf:plan:add',     '#', 'admin', sysdate()),
('计划修改', @parentId, '3', '#', '', 'F', '0', '1', 'perf:plan:edit',    '#', 'admin', sysdate()),
('计划删除', @parentId, '4', '#', '', 'F', '0', '1', 'perf:plan:remove',  '#', 'admin', sysdate()),
('计划发布', @parentId, '5', '#', '', 'F', '0', '1', 'perf:plan:publish', '#', 'admin', sysdate()),
('结束考核', @parentId, '6', '#', '', 'F', '0', '1', 'perf:plan:finish',  '#', 'admin', sysdate()),
('计划导出', @parentId, '7', '#', '', 'F', '0', '1', 'perf:plan:export',  '#', 'admin', sysdate());

-- 我的绩效
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
values('我的绩效', @perfRoot, '4', '/perf/self', '', 'C', '0', '1', 'perf:self:view', 'fa fa-user-circle-o', 'admin', sysdate(), '我的绩效菜单');
select @selfMenu := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time) values
('我的考核查询', @selfMenu, '1', '#', '', 'F', '0', '1', 'perf:self:list',     '#', 'admin', sysdate()),
('员工自评',     @selfMenu, '2', '#', '', 'F', '0', '1', 'perf:self:evaluate', '#', 'admin', sysdate());

-- 绩效评分
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
values('绩效评分', @perfRoot, '5', '/perf/review', '', 'C', '0', '1', 'perf:review:view', 'fa fa-check-square-o', 'admin', sysdate(), '绩效评分菜单');
select @parentId := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time) values
('评分查询', @parentId, '1', '#', '', 'F', '0', '1', 'perf:review:list',  '#', 'admin', sysdate()),
('上级评分', @parentId, '2', '#', '', 'F', '0', '1', 'perf:review:score', '#', 'admin', sysdate());

-- 绩效结果
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time, remark)
values('绩效结果', @perfRoot, '6', '/perf/result', '', 'C', '0', '1', 'perf:result:view', 'fa fa-bar-chart', 'admin', sysdate(), '绩效结果菜单');
select @parentId := LAST_INSERT_ID();
insert into sys_menu (menu_name, parent_id, order_num, url, target, menu_type, visible, is_refresh, perms, icon, create_by, create_time) values
('结果查询', @parentId, '1', '#', '', 'F', '0', '1', 'perf:result:list',   '#', 'admin', sysdate()),
('结果导出', @parentId, '2', '#', '', 'F', '0', '1', 'perf:result:export', '#', 'admin', sysdate());

-- 普通角色默认拥有「我的绩效」
insert into sys_role_menu (role_id, menu_id)
select 2, @perfRoot from dual where exists (select 1 from sys_role where role_id = 2)
union all
select 2, menu_id from sys_menu where (menu_id = @selfMenu or parent_id = @selfMenu) and exists (select 1 from sys_role where role_id = 2);
