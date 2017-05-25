-- ----------------------------
-- Table structure for PCM_SYS_CODE_RULE
-- ----------------------------
DROP TABLE "PCM_SYS_CODE_RULE";
CREATE TABLE "PCM_SYS_CODE_RULE" (
"ID" NUMBER NOT NULL ,
"TYPE" VARCHAR2(50 CHAR) NOT NULL ,
"TABLE_NAME" VARCHAR2(30 CHAR) NULL ,
"COLUMN_NAME" VARCHAR2(30 CHAR) NULL ,
"RULE" VARCHAR2(100 CHAR) NOT NULL ,
"EXAMPLE" VARCHAR2(50 CHAR) NOT NULL ,
"REMARK" VARCHAR2(500 CHAR) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON TABLE "PCM_SYS_CODE_RULE" IS '系统编码生成规则';
COMMENT ON COLUMN "PCM_SYS_CODE_RULE"."TYPE" IS '业务类型';
COMMENT ON COLUMN "PCM_SYS_CODE_RULE"."TABLE_NAME" IS '业务表名';
COMMENT ON COLUMN "PCM_SYS_CODE_RULE"."COLUMN_NAME" IS '列名';
COMMENT ON COLUMN "PCM_SYS_CODE_RULE"."RULE" IS '编码规则';

-- ----------------------------
-- Records of PCM_SYS_CODE_RULE
-- ----------------------------
INSERT INTO "PCM_SYS_CODE_RULE" VALUES ('1', '完整选配清单单号', 'PCM_CHOICE_LIST_HW', 'CHOICE_CODE', '[fixed=CPC;date=yyyyMMdd;serial=4]', 'CPC201703270001', null);
INSERT INTO "PCM_SYS_CODE_RULE" VALUES ('3', '软件选配编号', 'PCM_CHOICE_LIST_SW', 'SOFTWARE_CODE', '[fixed=SW;date=yyyyMMdd;serial=3]', 'SW20170404001', null);
INSERT INTO "PCM_SYS_CODE_RULE" VALUES ('2', '选配单配置码', 'PCM_CHOICE_LIST_HW', 'CONFIG_CODE', '[var=.;var=.;serial=7]', '50.301.0000001', null);
INSERT INTO "PCM_SYS_CODE_RULE" VALUES ('4', '印刷品选配编号', 'PCM_CHOICE_LIST_PT', 'PRINT_CODE', '[fixed=PT;date=yyyyMMdd;serial=3]', 'PT20170404001', null);

-- ----------------------------
-- Indexes structure for table PCM_SYS_CODE_RULE
-- ----------------------------

-- ----------------------------
-- Checks structure for table PCM_SYS_CODE_RULE
-- ----------------------------
ALTER TABLE "PCM_SYS_CODE_RULE" ADD CHECK ("ID" IS NOT NULL);
ALTER TABLE "PCM_SYS_CODE_RULE" ADD CHECK ("TYPE" IS NOT NULL);
ALTER TABLE "PCM_SYS_CODE_RULE" ADD CHECK ("RULE" IS NOT NULL);
ALTER TABLE "PCM_SYS_CODE_RULE" ADD CHECK ("EXAMPLE" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table PCM_SYS_CODE_RULE
-- ----------------------------
ALTER TABLE "PCM_SYS_CODE_RULE" ADD PRIMARY KEY ("ID");
