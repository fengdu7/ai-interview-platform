# 04 · 后端 API（AI PM 视角）

> **文档定位**：讲清「每个接口是干什么的」。PM 不用写接口，但要能说清每个接口的业务功能，以及几个核心交互流程。
>
> **产品定位**：C 端求职者的 AI 面试陪练。接口服务于简历分析、语音面试、定向备考、RAG 问答四大场景，覆盖 8 个控制器、73 个接口。

---

## 一、统一响应规范

所有接口统一返回 `Result<T>`，**HTTP 状态码始终 200**，业务状态在 body 的 `code` 里：

```json
{
  "code": 200,
  "message": "success",
  "data": { }
}
```

- 成功：`code = 200`
- 业务失败：`code` 对应具体 `ErrorCode`，`message` 为可读描述

> **PM 要点**：这是「HTTP 200 + 业务码」约定——前端不靠 HTTP 状态码判断业务成败，统一解析 `body.code`。

---

## 二、全部接口清单（按模块，逐个说明功能）

### 2.1 简历模块 `/api/resumes`

| 方法 | 路径 | 功能说明 |
|---|---|---|
| POST | `/api/resumes/upload` | 上传简历文件（multipart），上传后自动触发异步分析 |
| GET | `/api/resumes` | 获取所有简历列表（简历库页） |
| GET | `/api/resumes/{id}/detail` | 获取简历详情（含分析历史与评分） |
| GET | `/api/resumes/{id}/export` | 导出简历分析报告为 PDF |
| DELETE | `/api/resumes/{id}` | 删除简历 |
| POST | `/api/resumes/{id}/reanalyze` | 重新分析简历（分析失败后手动重试） |
| GET | `/api/resumes/health` | 简历服务健康检查 |

### 2.2 面试方向（Skill）模块 `/api/interview/skills`

| 方法 | 路径 | 功能说明 |
|---|---|---|
| GET | `/api/interview/skills` | 列出所有内置面试方向（Java 后端、前端等 10+） |
| GET | `/api/interview/skills/{id}` | 获取单个面试方向的详情（考察范围、难度分布） |
| POST | `/api/interview/skills/parse-jd` | 解析岗位 JD，返回推荐的考察分类（自定义面试出题） |

### 2.3 面试安排模块 `/api/interview-schedule`

| 方法 | 路径 | 功能说明 |
|---|---|---|
| POST | `/api/interview-schedule/parse` | 解析面试邀约文本（提取公司/岗位/时间/会议链接） |
| POST | `/api/interview-schedule` | 创建一条面试日程记录 |
| GET | `/api/interview-schedule` | 获取日程列表（支持状态/时间范围筛选） |
| GET | `/api/interview-schedule/{id}` | 获取单条面试记录详情 |
| PUT | `/api/interview-schedule/{id}` | 更新面试记录 |
| DELETE | `/api/interview-schedule/{id}` | 删除面试记录 |
| PATCH/PUT | `/api/interview-schedule/{id}/status` | 更新面试状态（待面试/已完成/已取消） |

### 2.4 知识库模块 `/api/knowledgebase`

| 方法 | 路径 | 功能说明 |
|---|---|---|
| POST | `/api/knowledgebase/upload` | 上传知识库文档（触发异步向量化） |
| GET | `/api/knowledgebase/list` | 获取知识库列表（支持排序、向量化状态筛选） |
| GET | `/api/knowledgebase/{id}` | 获取单个知识库详情 |
| DELETE | `/api/knowledgebase/{id}` | 删除知识库 |
| POST | `/api/knowledgebase/query` | 基于知识库回答问题（同步返回完整答案） |
| POST | `/api/knowledgebase/query/stream` | 基于知识库回答问题（SSE 流式，打字机效果） |
| GET | `/api/knowledgebase/{id}/download` | 下载知识库原始文件 |
| POST | `/api/knowledgebase/{id}/revectorize` | 重新向量化（向量化失败后手动重试） |
| GET | `/api/knowledgebase/categories` | 获取所有分类 |
| GET | `/api/knowledgebase/category/{category}` | 按分类获取知识库列表 |
| GET | `/api/knowledgebase/uncategorized` | 获取未分类的知识库 |
| PUT | `/api/knowledgebase/{id}/category` | 更新知识库分类 |
| GET | `/api/knowledgebase/search` | 按关键字搜索知识库 |
| GET | `/api/knowledgebase/stats` | 获取知识库统计信息 |

### 2.5 知识库题库与面试模块

| 方法 | 路径 | 功能说明 |
|---|---|---|
| GET | `/api/knowledgebase/{id}/questions` | 获取知识库题目列表（按状态/分类/难度/关键字筛选） |
| GET | `/api/knowledgebase/{id}/questions/categories` | 获取题目分类统计（各分类题目数） |
| POST | `/api/knowledgebase/{id}/questions/generate` | 异步生成题目（从知识库内容出题） |
| GET | `/api/knowledgebase/{id}/questions/generation-status` | 查询题目生成进度 |
| POST | `/api/knowledgebase/{id}/questions` | 手动新增一道题目 |
| PUT | `/api/knowledgebase/questions/{questionId}` | 编辑题目 |
| PUT | `/api/knowledgebase/questions/{questionId}/status` | 修改题目状态（草稿/启用/归档） |
| DELETE | `/api/knowledgebase/questions/{questionId}` | 删除题目 |
| GET | `/api/knowledgebase/{id}/interview-capacity` | 面试容量校验（计算可用题目数是否满足需求） |
| POST | `/api/knowledgebase-interviews/sessions` | 创建知识库专项面试 |

### 2.6 RAG 问答模块 `/api/rag-chat/sessions`

| 方法 | 路径 | 功能说明 |
|---|---|---|
| POST | `/api/rag-chat/sessions` | 创建 RAG 问答会话 |
| GET | `/api/rag-chat/sessions` | 获取会话列表 |
| GET | `/api/rag-chat/sessions/{sessionId}` | 获取会话详情（含消息历史） |
| PUT | `/api/rag-chat/sessions/{sessionId}/title` | 更新会话标题 |
| PUT | `/api/rag-chat/sessions/{sessionId}/pin` | 切换会话置顶状态 |
| PUT | `/api/rag-chat/sessions/{sessionId}/knowledge-bases` | 更新会话关联的知识库 |
| DELETE | `/api/rag-chat/sessions/{sessionId}` | 删除会话 |
| POST | `/api/rag-chat/sessions/{sessionId}/messages/stream` | 发送消息（SSE 流式返回） |

### 2.7 语音面试模块 `/api/voice-interview`

| 方法 | 路径 | 功能说明 |
|---|---|---|
| POST | `/api/voice-interview/sessions` | 创建语音面试会话（选方向/难度/时长/环节开关） |
| GET | `/api/voice-interview/sessions/{sessionId}` | 获取语音会话详情 |
| GET | `/api/voice-interview/sessions` | 获取语音会话列表（按用户/状态筛选） |
| POST | `/api/voice-interview/sessions/{sessionId}/end` | 结束面试（触发异步评估） |
| PUT | `/api/voice-interview/sessions/{sessionId}/pause` | 暂停面试 |
| PUT | `/api/voice-interview/sessions/{sessionId}/resume` | 恢复面试 |
| GET | `/api/voice-interview/sessions/{sessionId}/messages` | 获取对话消息历史 |
| GET | `/api/voice-interview/sessions/{sessionId}/evaluation` | 查询评估状态与结果（轮询） |
| POST | `/api/voice-interview/sessions/{sessionId}/evaluation` | 触发异步评估 |
| DELETE | `/api/voice-interview/sessions/{sessionId}` | 删除语音会话 |

> 注：语音面试的**实时音频**不走 REST，走 WebSocket：`/ws/voice-interview/{sessionId}`。

---

## 三、三个核心交互流程（请求/响应示例）

### 示例 1：简历上传 + 异步分析（异步模式）

**Step 1 — 上传**：

```http
POST /api/resumes/upload
Content-Type: multipart/form-data
# file = resume.pdf
```

响应（异步任务已提交，返回状态 PENDING）：

```json
{
  "code": 200,
  "message": "success",
  "data": {
    "resumeId": 1,
    "analyzeStatus": "PENDING",
    "duplicate": false
  }
}
```

**Step 2 — 轮询**：前端定时拉 `GET /api/resumes/{id}/detail`，直到 `analyzeStatus = COMPLETED`。

**Step 3 — 取结果**：详情里含 `overall_score`、5 个维度分、strengths/suggestions。

> **PM 要点**：「提交 → 拿任务 ID → 轮询状态 → 取结果」是异步 LLM 任务的标准模式。简历分析、向量化、出题、评估全是这个套路。

### 示例 2：RAG 流式问答（SSE 流式）

```http
POST /api/rag-chat/sessions/{sessionId}/messages/stream
Content-Type: application/json
Accept: text/event-stream

{ "question": "什么是 CAP 定理？" }
```

响应（SSE 流，逐 chunk 返回，打字机效果）：

```
data: 什么是
data: 什么是CAP
data: 什么是CAP定理
...
```

> **PM 要点**：SSE 流式是「AI 产品体验」的关键——用户不愿等 5 秒才看到完整答案，打字机式输出显著提升感知速度。

### 示例 3：语音面试（REST + WebSocket 混合）

- **REST 管生命周期**：`POST /api/voice-interview/sessions` 创建、`PUT /{id}/pause` 暂停、`POST /{id}/end` 结束
- **WebSocket 传实时音频**：`/ws/voice-interview/{sessionId}`
- **评估异步**：`POST /{id}/end` 触发 → `GET /{id}/evaluation` 轮询（PENDING/PROCESSING/COMPLETED）

> **PM 要点**：实时性要求（音频）用 WebSocket，非实时控制（创建/暂停/结束）用 REST，重计算（评估）用异步——三类交互模式并存，是理解「技术边界」的最好样本。

---

## 四、RESTful 设计规范总结（PM 要能讲）

- **资源命名**：名词复数，层级清晰（`/api/voice-interview/sessions/{sessionId}/messages`）
- **HTTP 方法语义**：GET 查、POST 增/动作、PUT 全量改、PATCH 精确改
- **统一响应**：`Result<T>`，HTTP 200 + `code` 区分业务成败
- **限流**：`@RateLimit` 注解，Global / IP / User 三维度（如上传简历 Global=5 次/分钟）
- **幂等**：`file_hash` 去重（相同文件不重复处理）
- **异步约定**：长耗时 LLM 任务一律「提交 + 轮询」
