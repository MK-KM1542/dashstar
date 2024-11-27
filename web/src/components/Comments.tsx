// 评论区组件

import { Avatar, Box, Button, Card, Divider, Grid2, TextField, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import { api } from "@/utils/axios.ts";
import { Comment } from "@/models/comment.ts";
import useAuthStore from "@/stores/auth.ts";
import { Delete } from "@mui/icons-material";


function MyComment({ comment, firstName, created_at, commentId, fetchComments, }: {
    comment?: string; firstName?: string; created_at?: number; commentId?: number;fetchComments: () => void;
}) {
    const date = new Date(Number(created_at) * 1000);
    const authStore = useAuthStore();

    function delComment() {
        console.log("del");
        api().delete(`/comments/${commentId}`)
            .then(() => {
                fetchComments();
            })
            .catch((error) => {
                // 请求失败后的处理逻辑
                console.error("删除评论失败:", error);
            });
    }

    return (
        <>
            <Grid2 container sx={{ backgroundColor: "#f0f0f02d" }}>
                <Grid2 display="flex" sx={{ margin: 2 }}>
                    <Avatar sx={{ marginRight: 4, alignContent: "center" }}>
                        {firstName?.slice(0, 4)}
                    </Avatar>
                    <Box>
                        {comment ? (
                            <Typography variant="body1">{comment}</Typography>
                        ) : (<Typography variant="body2" fontStyle="italic">...</Typography>)}
                        <Typography variant="caption">{date.toLocaleString()}</Typography>
                    </Box>
                </Grid2>
                {authStore.user?.role==="admin" && (
                    <Button onClick={delComment}>
                        <Delete />
                    </Button>
                )}
            </Grid2>
        </>
    );
}

export default function Comments({ id }: { id: number }) {

    const [comments, setComments] = useState<Array<Comment>>();
    const [commentSend, setCommentSend] = useState<string>();
    const authStore = useAuthStore();

    function fetchComments() {
        api().get(`/articles/${id}/comments`).then(
            (res) => {
                const r = res.data;
                setComments(r.data);
            },
        );
    }

    useEffect(() => {
        fetchComments();
    }, []);

    function submitComment() {
        api().post("/comments", {
            article_id: id,
            content: commentSend,
            user_id: authStore?.user?.id,
        }).then(() => {
            fetchComments();
            setCommentSend("");
        });
    }

    return (
        <>
            <Divider>评论区</Divider>
            <Card sx={{ marginTop: 3 }}>
                <Box sx={{
                    display: "flex",
                    justifyContent: "space-between",
                    margin: "1rem 0",
                    gap: "1rem",
                }}>
                    <TextField
                        maxRows="20"
                        minRows="2"
                        multiline
                        fullWidth
                        value={commentSend}
                        onChange={(e) => setCommentSend(e.target.value)}
                    />
                    <Button variant="contained" onClick={submitComment}>评论</Button>
                </Box>
                <Grid2>
                    {comments?.map((oneOfComments, index) => {
                        return (
                            <>

                                <MyComment
                                    key={index}
                                    comment={oneOfComments.content}
                                    firstName={oneOfComments.user?.username}
                                    created_at={oneOfComments.created_at}
                                    commentId={oneOfComments.id}
                                    fetchComments={fetchComments}

                                />
                            </>
                        );
                    })}
                </Grid2>
            </Card>
        </>
    );
}
