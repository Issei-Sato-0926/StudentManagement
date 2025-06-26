# Student Management（受講生管理システム）

## 概要

受講生の個人情報や受講コース、申込状況を管理するWebアプリケーションです。<br>
受講生・受講コース・申込状況を紐付けて扱い、検索・登録・更新といった処理を行います。

## 作成背景

様々なWebアプリケーションで使用されるCRUD処理を実装し、バックエンドの知識や設計力を深めたいと考え作成しました。<br>
実装からテスト・デプロイまで一貫して取り組んだ成果物を提示できるよう構成しています。

## 工夫点

- Controller、Service、Repository層で役割を明確に分担し、今後の機能追加や保守がしやすい構成
- リストの結合（StudentConverter）によるドメイン（StudentDetail）の組み立て

## 使用技術・ライブラリ

| 分類     | 技術スタック                                                      |
|--------|-------------------------------------------------------------|
| バックエンド | Java 21 / Spring Boot 3.4.5                                 |
| ORマッパー | MyBatis                                                     |
| ビルド    | Gradle                                                      |
| テスト    | JUnit5 / Mockito / AssertJ / MyBatis Test / Bean Validation |
| CI/CD  | GitHub Actions / Amazon EC2 / systemd                       |
| データベース | H2（テスト用） / MySQL（本番想定）                                      |

## 機能一覧

- 受講生検索（全件・ID指定・条件指定）
- 受講生登録（コース情報・申込状況を含む）
- 受講生更新（コース情報・申込状況を含む）
- 受講生削除（論理削除）
- 入力バリデーション（形式・必須・制約チェック）

## API

## 動作確認用URL

https://yourusername.github.io/student-management-frontend/index.html

## テスト実装

| レイヤー        | 主なテスト内容                  | 使用技術                             |
|-------------|--------------------------|----------------------------------|
| Controller層 | HTTPリクエスト、バリデーション、JSON検証 | MockMvc / WebMvcTest / Validator |
| Service層    | 業務ロジック、例外発生、引数の整合性       | JUnit5 / Mockito                 |
| Repository層 | SQLマッピングとDB操作の検証         | MyBatis Test / H2 DB             |

- 異常系含む網羅的なバリデーションテスト
- Mockitoによる依存オブジェクトのスタブ化で純粋なユニットテストを実現

## CI / CD パイプライン構成

GitHub Actions により、テスト → ビルド → EC2への自動デプロイを実現しています。

### 構成概要

| 項目    | 内容                                     |
|-------|----------------------------------------|
| トリガー  | `main`ブランチへの push / PR                 |
| ビルド   | `./gradlew bootJar`                    |
| テスト   | `./gradlew test`                       |
| デプロイ先 | Amazon EC2 (Amazon Linux 2023)         |
| 起動管理  | systemd による `StudentManagement` サービス制御 |

## 今後の展望

* 認証機能の追加
* HTTPSプロトコルへの対応
* フロントエンド実装（Thymeleaf, Vue.js, React など）