//
//  WatchListViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/12/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "WatchListViewController.h"
int count;
int count2;
NSMutableArray *items;

@interface WatchListViewController ()

@end

@implementation WatchListViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    PFQuery *query = [PFQuery queryWithClassName:@"itemInfo"];
    [query whereKey:@"user" equalTo:[PFUser currentUser]];
    [query findObjectsInBackgroundWithBlock:^(NSArray *objects, NSError *error) {
        items = [[NSMutableArray alloc] init];
        if (!error) {
            for (PFObject *object in objects) {
                [items addObject:[object objectForKey:@"title"]];
                count++;
            }
            [myTableView reloadData];
        } else {
            NSString *errorString = [error userInfo][@"error"];
            // show the errorString
            UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Error"
                                                            message:errorString
                                                           delegate:nil
                                                  cancelButtonTitle:@"OK"
                                                  otherButtonTitles:nil];
            [alert show];
        }
        
    }];
    
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)viewWillAppear:(BOOL)animated {
    count = 0;
    count2 = 0;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"SimpleTableCell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    cell.textLabel.textColor = [UIColor darkGrayColor];
    
    cell.textLabel.text = [NSString stringWithFormat:@"%@", [items objectAtIndex:count2]];
    count2++;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
}

-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton *)sender;
    if (button.tag == 0) {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        
        WatchListViewController *addMovieView = [storyBoard instantiateViewControllerWithIdentifier:@"AddMovieView"];
        [self presentViewController:addMovieView animated:true completion:nil];
    } else {
        UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
        
        WatchListViewController *profileView = [storyBoard instantiateViewControllerWithIdentifier:@"ProfileView"];
        [self presentViewController:profileView animated:true completion:nil];
    }
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
